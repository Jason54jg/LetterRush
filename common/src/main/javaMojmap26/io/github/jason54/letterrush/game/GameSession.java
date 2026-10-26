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
    private final MinecraftServer server;
    private final char letter;
    private final int durationSeconds;
    private final long endGameTimeMs;
    private final ServerBossEvent bossBar;
    private final Random random = new Random();
    private boolean finished;

    public GameSession(MinecraftServer server, char letter, int durationSeconds) {
        this.server = server;
        this.letter = letter;
        this.durationSeconds = durationSeconds;
        this.endGameTimeMs = System.currentTimeMillis() + durationSeconds * 1000L;
        this.bossBar = new ServerBossEvent(
                UUID.randomUUID(),
                Component.translatable("letterrush.bossbar.title", String.valueOf(letter)),
                BossEvent.BossBarColor.YELLOW,
                BossEvent.BossBarOverlay.PROGRESS
        );
        this.bossBar.setDarkenScreen(false);
        this.bossBar.setCreateWorldFog(false);
        syncBossBarPlayers();
        updateBossProgress();
    }

    public MinecraftServer server() {
        return server;
    }

    public char letter() {
        return letter;
    }

    public int durationSeconds() {
        return durationSeconds;
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

    public int secondsRemaining() {
        long remainingMs = endGameTimeMs - System.currentTimeMillis();
        return (int) Math.max(0, Math.ceil(remainingMs / 1000.0));
    }

    public boolean isTimeUp() {
        return System.currentTimeMillis() >= endGameTimeMs;
    }

    public void syncBossBarPlayers() {
        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for (ServerPlayer player : players) {
            if (!bossBar.getPlayers().contains(player)) {
                bossBar.addPlayer(player);
            }
        }
        bossBar.getPlayers().removeIf(player -> !players.contains(player));
        updateBossProgress();
    }

    public void updateBossProgress() {
        float progress = durationSeconds <= 0 ? 0f : secondsRemaining() / (float) durationSeconds;
        bossBar.setProgress(Math.max(0f, Math.min(1f, progress)));
    }

    public void clearBossBar() {
        bossBar.removeAllPlayers();
        bossBar.setVisible(false);
    }

    public Random random() {
        return random;
    }

    public RewardDefinition preselectedReward;
}
