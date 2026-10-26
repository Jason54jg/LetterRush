package io.github.jason54.letterrush.game;

import io.github.jason54.letterrush.reward.RewardDefinition;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Random;

public final class GameSession {
    private final MinecraftServer server;
    private final char letter;
    private final int durationSeconds;
    private final long endGameTimeMs;
    private final ServerBossBar bossBar;
    private final Random random = new Random();
    private boolean finished;

    public GameSession(MinecraftServer server, char letter, int durationSeconds) {
        this.server = server;
        this.letter = letter;
        this.durationSeconds = durationSeconds;
        this.endGameTimeMs = System.currentTimeMillis() + durationSeconds * 1000L;
        this.bossBar = new ServerBossBar(
                Text.translatable("letterrush.bossbar.title", String.valueOf(letter)),
                BossBar.Color.YELLOW,
                BossBar.Style.PROGRESS
        );
        this.bossBar.setDarkenSky(false);
        this.bossBar.setThickenFog(false);
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

    public ServerBossBar bossBar() {
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
        List<ServerPlayerEntity> players = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : players) {
            if (!bossBar.getPlayers().contains(player)) {
                bossBar.addPlayer(player);
            }
        }
        bossBar.getPlayers().removeIf(player -> !players.contains(player));
        updateBossProgress();
    }

    public void updateBossProgress() {
        float progress = durationSeconds <= 0 ? 0f : secondsRemaining() / (float) durationSeconds;
        bossBar.setPercent(Math.max(0f, Math.min(1f, progress)));
    }

    public void clearBossBar() {
        bossBar.clearPlayers();
        bossBar.setVisible(false);
    }

    public Random random() {
        return random;
    }

    public RewardDefinition preselectedReward;
}
