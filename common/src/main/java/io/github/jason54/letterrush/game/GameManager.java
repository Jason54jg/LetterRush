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
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public final class GameManager {
    private static GameManager instance = new GameManager();
    private GameSession session;

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

    public boolean start(MinecraftServer server, int durationSeconds) {
        if (isRunning()) {
            return false;
        }
        List<Character> pool = LetterRushConfig.get().letterPool();
        char letter = pool.get(server.getOverworld().getRandom().nextInt(pool.size()));
        session = new GameSession(server, letter, durationSeconds);
        session.preselectedReward = RewardRoller.roll(session.random());

        broadcastStart(server, letter, durationSeconds);
        TitleHelper.showRoundStart(server, letter);
        LetterRush.LOGGER.info("LetterRush started: letter={}, duration={}s", letter, durationSeconds);
        return true;
    }

    public void stop(MinecraftServer server, boolean manual) {
        if (session == null) {
            return;
        }
        endSession(server, null, null, null, manual ? StopReason.MANUAL : StopReason.OTHER);
    }

    public void tick(MinecraftServer server) {
        if (session == null || session.isFinished()) {
            return;
        }
        session.syncBossBarPlayers();
        session.updateBossProgress();
        if (session.isTimeUp()) {
            endSession(server, null, null, null, StopReason.TIMEOUT);
        }
    }

    /**
     * Called from platform-specific pickup hooks when a player collects an item entity.
     */
    public void onPlayerPickup(ServerPlayerEntity player, ItemStack stack) {
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
        endSession(server, player, matchName, reward, StopReason.WIN);
    }

    private void endSession(
            MinecraftServer server,
            ServerPlayerEntity winner,
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
            case TIMEOUT -> handleTimeout(server, letter);
            case MANUAL -> broadcast(server, LetterRushText.message("stopped", "letterrush.chat.stopped").formatted(Formatting.YELLOW));
            default -> {
            }
        }
        session = null;
    }

    private void handleWin(MinecraftServer server, ServerPlayerEntity winner, String itemName, RewardDefinition reward, char letter) {
        RewardExecutor.grant(server, winner, reward);
        if (LetterRushConfig.get().playSounds) {
            ServerWorld winnerWorld = ServerPlayerAccess.world(winner);
            winnerWorld.playSound(null, winner.getBlockPos(), SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.PLAYERS, 1f, 1f);
            server.getPlayerManager().getPlayerList().forEach(p ->
                    ServerPlayerAccess.world(p).playSound(null, p.getBlockPos(), SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 0.6f, 1.2f));
        }
        if (LetterRushConfig.get().winnerParticles) {
            spawnWinnerParticles(winner);
        }

        Text chat = LetterRushText.message(
                "winner",
                "letterrush.chat.winner",
                winner.getName().getString(),
                itemName,
                reward.displayLabel()
        ).formatted(Formatting.GOLD);
        broadcast(server, chat);
        TitleHelper.showWinner(server, winner.getName().getString(), reward.displayLabel());

        if (LetterRushConfig.get().rouletteAnimation) {
            List<String> labels = rewardDisplayLabels();
            int winnerIndex = indexOfReward(labels, reward.displayLabel());
            RoulettePayload payload = new RoulettePayload(winner.getUuid(), labels, winnerIndex, LetterRushConfig.get().rouletteTicks);
            LetterRushNetworking.sendRoulette(server, payload);
        }
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

    private void handleTimeout(MinecraftServer server, char letter) {
        broadcast(server, LetterRushText.message("time_up", "letterrush.chat.time_up", String.valueOf(letter)).formatted(Formatting.RED));
        TitleHelper.showTimeUp(server);
        if (LetterRushConfig.get().playSounds) {
            server.getPlayerManager().getPlayerList().forEach(p ->
                    ServerPlayerAccess.world(p).playSound(null, p.getBlockPos(), SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM.value(), SoundCategory.PLAYERS, 0.8f, 0.5f));
        }
    }

    private void broadcastStart(MinecraftServer server, char letter, int durationSeconds) {
        broadcast(server, LetterRushText.message(
                "start",
                "letterrush.chat.start",
                String.valueOf(letter),
                durationSeconds
        ).formatted(Formatting.GREEN));
    }

    private void broadcast(MinecraftServer server, Text message) {
        server.getPlayerManager().broadcast(message, false);
    }

    private void spawnWinnerParticles(ServerPlayerEntity winner) {
        ServerWorld world = ServerPlayerAccess.world(winner);
        world.spawnParticles(
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
        TIMEOUT,
        MANUAL,
        OTHER
    }
}
