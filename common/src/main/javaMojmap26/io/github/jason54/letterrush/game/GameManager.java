package io.github.jason54.letterrush.game;

import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.config.LetterRushConfig;
import io.github.jason54.letterrush.network.LetterRushNetworking;
import io.github.jason54.letterrush.network.RoulettePayload;
import io.github.jason54.letterrush.reward.RewardDefinition;
import io.github.jason54.letterrush.reward.RewardExecutor;
import io.github.jason54.letterrush.reward.RewardRoller;
import io.github.jason54.letterrush.util.LetterRushText;
import io.github.jason54.letterrush.util.ServerPlayerAccess;
import io.github.jason54.letterrush.util.TitleHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

import java.util.List;

public final class GameManager {
    private static final int POST_ROULETTE_PAUSE_TICKS = 40;

    private static GameManager instance = new GameManager();
    private GameSession session;
    /** Server ticks until auto-start after a win; {@code -1} = inactive. */
    private int ticksUntilRestart = -1;

    public static GameManager get(MinecraftServer server) {
        return instance;
    }

    public static void reset() {
        instance = new GameManager();
    }

    public boolean isRunning() {
        return session != null && !session.isFinished();
    }

    public GameSession session() {
        return session;
    }

    public boolean start(MinecraftServer server) {
        if (isRunning()) {
            return false;
        }
        ticksUntilRestart = -1;
        List<Character> pool = EnglishItemNames.playableLetterPool(server, LetterRushConfig.get().letterPool());
        if (pool.isEmpty()) {
            LetterRush.LOGGER.error("LetterRush cannot start: no items with letter names found.");
            return false;
        }
        char letter = pool.get(server.overworld().getRandom().nextInt(pool.size()));
        session = new GameSession(server, letter);
        session.preselectedReward = RewardRoller.roll(session.random());

        broadcastStart(server, letter);
        TitleHelper.showRoundStart(server, letter);
        LetterRush.LOGGER.info("LetterRush started: letter={}", letter);
        return true;
    }

    public void stop(MinecraftServer server, boolean manual) {
        ticksUntilRestart = -1;
        if (session == null) {
            return;
        }
        endSession(server, null, null, null, manual ? StopReason.MANUAL : StopReason.OTHER);
    }

    public void tick(MinecraftServer server) {
        if (session != null && !session.isFinished()) {
            session.syncBossBarPlayers();
            session.updateBossProgress();
        }
        if (ticksUntilRestart > 0) {
            ticksUntilRestart--;
            if (ticksUntilRestart == 0) {
                ticksUntilRestart = -1;
                if (!isRunning()) {
                    start(server);
                }
            }
        }
    }

    /**
     * Called from platform-specific pickup hooks when a player collects an item entity from the world.
     * Items already in inventory do not count — drop and pick up again to win.
     */
    public void onPlayerPickup(ServerPlayer player, ItemStack stack) {
        if (!isRunning() || stack.isEmpty()) {
            return;
        }
        MinecraftServer server = ServerPlayerAccess.server(player);
        if (server == null) {
            return;
        }
        char letter = session.letter();
        if (!EnglishItemNames.startsWithLetter(server, stack, letter)) {
            return;
        }
        String matchName = EnglishItemNames.getMatchName(server, stack);
        RewardDefinition reward = session.preselectedReward != null
                ? session.preselectedReward
                : RewardRoller.roll(session.random());
        LetterRush.LOGGER.info("LetterRush win: player={} item={} letter={}", player.getScoreboardName(), matchName, letter);
        endSession(server, player, matchName, reward, StopReason.WIN);
    }

    private void endSession(
            MinecraftServer server,
            ServerPlayer winner,
            String itemName,
            RewardDefinition reward,
            StopReason reason
    ) {
        if (session == null || session.isFinished()) {
            return;
        }
        session.markFinished();
        char letter = session.letter();
        session.clearBossBar();

        switch (reason) {
            case WIN -> handleWin(server, winner, itemName, reward, letter);
            case MANUAL -> broadcast(server, LetterRushText.message("stopped", "letterrush.chat.stopped").withStyle(ChatFormatting.YELLOW));
            default -> {
            }
        }
        session = null;
    }

    private void handleWin(MinecraftServer server, ServerPlayer winner, String itemName, RewardDefinition reward, char letter) {
        RewardExecutor.grant(server, winner, reward);
        if (LetterRushConfig.get().playSounds) {
            ServerLevel winnerWorld = ServerPlayerAccess.world(winner);
            winnerWorld.playSound(winner, winner.blockPosition(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundSource.PLAYERS, 1f, 1f);
            server.getPlayerList().getPlayers().forEach(p ->
                    ServerPlayerAccess.world(p).playSound(p, p.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 0.6f, 1.2f));
        }
        if (LetterRushConfig.get().winnerParticles) {
            spawnWinnerParticles(winner);
        }

        Component chat = LetterRushText.message(
                "winner",
                "letterrush.chat.winner",
                winner.getDisplayName().getString(),
                itemName,
                reward.displayLabel()
        ).withStyle(ChatFormatting.GOLD);
        broadcast(server, chat);
        TitleHelper.showWinner(server, winner.getDisplayName().getString(), reward.displayLabel());

        if (LetterRushConfig.get().rouletteAnimation) {
            List<String> labels = rewardDisplayLabels();
            int winnerIndex = indexOfReward(labels, reward.displayLabel());
            int spinTicks = LetterRushConfig.get().rouletteTicks;
            RoulettePayload payload = new RoulettePayload(winner.getUUID(), labels, winnerIndex, spinTicks);
            LetterRushNetworking.sendRoulette(server, payload);
            scheduleRestartAfterWin(spinTicks + POST_ROULETTE_PAUSE_TICKS);
        } else {
            scheduleRestartAfterWin(POST_ROULETTE_PAUSE_TICKS);
        }
    }

    private void scheduleRestartAfterWin(int delayTicks) {
        ticksUntilRestart = Math.max(20, delayTicks);
        LetterRush.LOGGER.info("LetterRush will auto-restart in {} ticks", ticksUntilRestart);
    }

    private static List<String> rewardDisplayLabels() {
        return io.github.jason54.letterrush.reward.DatapackRewardLoader.activeRewards().stream()
                .map(RewardDefinition::displayLabel)
                .toList();
    }

    private static int indexOfReward(List<String> labels, String display) {
        for (int i = 0; i < labels.size(); i++) {
            if (labels.get(i).equals(display)) {
                return i;
            }
        }
        return 0;
    }

    private void broadcastStart(MinecraftServer server, char letter) {
        broadcast(server, LetterRushText.message(
                "start",
                "letterrush.chat.start",
                String.valueOf(letter)
        ).withStyle(ChatFormatting.GREEN));
    }

    private void broadcast(MinecraftServer server, Component message) {
        server.getPlayerList().broadcastSystemMessage(message, false);
    }

    private void spawnWinnerParticles(ServerPlayer winner) {
        ServerLevel world = ServerPlayerAccess.world(winner);
        world.sendParticles(
                ParticleTypes.TOTEM_OF_UNDYING,
                winner.getX(),
                winner.getY() + 1.0,
                winner.getZ(),
                40,
                0.5,
                0.8,
                0.5,
                0.15
        );
    }

    private enum StopReason {
        WIN,
        MANUAL,
        OTHER
    }
}
