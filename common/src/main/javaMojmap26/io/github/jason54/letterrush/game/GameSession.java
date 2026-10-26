package io.github.jason54.letterrush.game;

import io.github.jason54.letterrush.reward.RewardDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class GameSession {
    /** Boss-bar loop length (seconds): drains then resets so players see the round is live. */
    private static final int BOSS_CYCLE_SECONDS = 60;

    private final MinecraftServer server;
    private final char letter;
    private final long startTimeMs;
    private final ServerBossEvent bossBar;
    private final Random random = new Random();
    private boolean finished;
    private int lastDisplayedElapsed = -1;

    public GameSession(MinecraftServer server, char letter) {
        this.server = server;
        this.letter = letter;
        this.startTimeMs = System.currentTimeMillis();
        this.bossBar = new ServerBossEvent(
                UUID.randomUUID(),
                bossBarTitle(0),
                BossEvent.BossBarColor.YELLOW,
                BossEvent.BossBarOverlay.PROGRESS
        );
        this.bossBar.setDarkenScreen(false);
        this.bossBar.setCreateWorldFog(false);
        this.bossBar.setProgress(1f);
        syncBossBarPlayers();
        updateBossProgress();
    }

    public MinecraftServer server() {
        return server;
    }

    public char letter() {
        return letter;
    }

    public boolean isFinished() {
        return finished;
    }

    public void markFinished() {
        this.finished = true;
    }

    public ServerBossEvent bossBar() {
        return bossBar;
    }

    public int secondsElapsed() {
        long elapsedMs = System.currentTimeMillis() - startTimeMs;
        return (int) Math.max(0, elapsedMs / 1000L);
    }

    public void syncBossBarPlayers() {
        List<ServerPlayer> online = server.getPlayerList().getPlayers();
        for (ServerPlayer player : online) {
            if (!bossBar.getPlayers().contains(player)) {
                bossBar.addPlayer(player);
            }
        }
        for (ServerPlayer player : List.copyOf(bossBar.getPlayers())) {
            if (!online.contains(player)) {
                bossBar.removePlayer(player);
            }
        }
        updateBossProgress();
    }

    public void updateBossProgress() {
        int elapsed = secondsElapsed();
        if (elapsed != lastDisplayedElapsed) {
            lastDisplayedElapsed = elapsed;
            bossBar.setName(bossBarTitle(elapsed));
        }
        // No hard timeout: cycle the bar each minute as a live-round signal.
        float cycle = BOSS_CYCLE_SECONDS;
        float remainingInCycle = cycle - (elapsed % BOSS_CYCLE_SECONDS);
        bossBar.setProgress(Math.max(0.02f, remainingInCycle / cycle));
    }

    public void clearBossBar() {
        bossBar.removeAllPlayers();
        bossBar.setVisible(false);
    }

    public Random random() {
        return random;
    }

    public RewardDefinition preselectedReward;

    private Component bossBarTitle(int elapsedSeconds) {
        return Component.translatable(
                "letterrush.bossbar.running",
                String.valueOf(letter),
                formatElapsed(elapsedSeconds)
        );
    }

    public static String formatElapsed(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }
}
