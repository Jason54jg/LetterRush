package io.github.jason54.letterrush.client;

import io.github.jason54.letterrush.network.RoulettePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.UUID;

/**
 * Lightweight client overlay that cycles reward labels before landing on the winner.
 */
public final class RouletteOverlay {
    private static List<String> labels = List.of();
    private static int winnerIndex;
    private static int ticksRemaining;
    private static UUID winnerId;

    private RouletteOverlay() {
    }

    public static void start(RoulettePayload payload) {
        labels = payload.labels();
        winnerIndex = payload.winnerIndex();
        ticksRemaining = payload.spinTicks();
        winnerId = payload.winnerId();
    }

    public static void clientTick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
        }
    }

    public static void render(GuiGraphicsExtractor context) {
        if (ticksRemaining <= 0 || labels.isEmpty()) {
            return;
        }
        Minecraft client = Minecraft.getInstance();
        if (client.player == null) {
            return;
        }
        int width = context.guiWidth();
        int y = 48;
        int index = computeDisplayIndex();
        String label = labels.get(Math.floorMod(index, labels.size()));
        Component line = Component.translatable("letterrush.roulette.spinning").append(" ").append(label);
        int textWidth = client.font.width(line);
        context.fill(width / 2 - textWidth / 2 - 6, y - 4, width / 2 + textWidth / 2 + 6, y + 12, 0xAA000000);
        context.centeredText(client.font, line, width / 2, y, 0xFFFFD700);
    }

    private static int computeDisplayIndex() {
        if (ticksRemaining <= 5) {
            return winnerIndex;
        }
        int elapsed = Math.max(1, ticksRemaining);
        return (winnerIndex + elapsed * 3) % labels.size();
    }

    public static boolean isActive() {
        return ticksRemaining > 0;
    }

    public static UUID winnerId() {
        return winnerId;
    }
}
