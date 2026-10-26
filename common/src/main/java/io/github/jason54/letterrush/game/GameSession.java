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
    private final long startTimeMs;
    private final ServerBossBar bossBar;
    private final Random random = new Random();
    private boolean finished;
    private int lastDisplayedElapsed = -1;

    public GameSession(MinecraftServer server, char letter) {
        this.server = server;
        this.letter = letter;
        this.startTimeMs = System.currentTimeMillis();
        this.bossBar = new ServerBossBar(
                bossBarTitle(0),
                BossBar.Color.YELLOW,
                BossBar.Style.PROGRESS
        );
        this.bossBar.setDarkenSky(false);
        this.bossBar.setThickenFog(false);
        this.bossBar.setPercent(1f);
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

    public ServerBossBar bossBar() {
        return bossBar;
    }

    public int secondsElapsed() {
        long elapsedMs = System.currentTimeMillis() - startTimeMs;
        return (int) Math.max(0, elapsedMs / 1000L);
    }

    public void syncBossBarPlayers() {
        List<ServerPlayerEntity> online = server.getPlayerManager().getPlayerList();
        for (ServerPlayerEntity player : online) {
            if (!bossBar.getPlayers().contains(player)) {
                bossBar.addPlayer(player);
            }
        }
        for (ServerPlayerEntity player : List.copyOf(bossBar.getPlayers())) {
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
        bossBar.setPercent(1f);
    }

    public void clearBossBar() {
        bossBar.clearPlayers();
        bossBar.setVisible(false);
    }

    public Random random() {
        return random;
    }

    public RewardDefinition preselectedReward;

    private Text bossBarTitle(int elapsedSeconds) {
        return Text.translatable(
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
