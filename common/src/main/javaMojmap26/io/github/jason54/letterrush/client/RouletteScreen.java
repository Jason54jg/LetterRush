package io.github.jason54.letterrush.client;

import io.github.jason54.letterrush.network.RoulettePayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Full-screen GUI that spins through reward labels and lands on the winner.
 */
public final class RouletteScreen extends Screen {
    private static final int PANEL_W = 240;
    private static final int PANEL_H = 156;
    private static final int HOLD_WINNER_TICKS = 25;
    private static final int VISIBLE_ROWS = 2;

    private final List<String> labels;
    private final int winnerIndex;
    private final UUID winnerId;
    private int ticksRemaining;
    private int holdTicks = -1;

    public RouletteScreen(RoulettePayload payload) {
        super(Component.translatable("letterrush.roulette.title"));
        this.labels = List.copyOf(new ArrayList<>(payload.labels()));
        this.winnerIndex = payload.winnerIndex();
        this.winnerId = payload.winnerId();
        this.ticksRemaining = Math.max(10, payload.spinTicks());
    }

    /** Fallback opener; Fabric/NeoForge clients prefer calling {@code gui.setScreen} directly. */
    public static void open(RoulettePayload payload) {
        Minecraft client = Minecraft.getInstance();
        if (client != null) {
            client.setScreenAndShow(new RouletteScreen(payload));
        }
    }

    public UUID winnerId() {
        return winnerId;
    }

    @Override
    public void tick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
            if (ticksRemaining == 0) {
                holdTicks = HOLD_WINNER_TICKS;
            }
            return;
        }
        if (holdTicks > 0) {
            holdTicks--;
            if (holdTicks == 0) {
                onClose();
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean isInGameUi() {
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.extractTransparentBackground(graphics);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        if (labels.isEmpty()) {
            return;
        }

        int cx = this.width / 2;
        int cy = this.height / 2;
        int left = cx - PANEL_W / 2;
        int top = cy - PANEL_H / 2;

        graphics.fill(left, top, left + PANEL_W, top + PANEL_H, 0xF0161620);
        graphics.outline(left, top, PANEL_W, PANEL_H, 0xFFD4AF37);
        graphics.outline(left + 2, top + 2, PANEL_W - 4, PANEL_H - 4, 0x66D4AF37);

        graphics.centeredText(this.font, this.title, cx, top + 12, 0xFFFFD700);

        int index = displayIndex();
        int rowH = 16;
        int midY = cy - 5;
        int reelLeft = left + 20;
        int reelRight = left + PANEL_W - 20;
        graphics.fill(reelLeft, midY - VISIBLE_ROWS * rowH - 4, reelRight, midY + (VISIBLE_ROWS + 1) * rowH + 2, 0xAA000000);

        for (int offset = -VISIBLE_ROWS; offset <= VISIBLE_ROWS; offset++) {
            int i = Math.floorMod(index + offset, labels.size());
            String text = labels.get(i);
            int y = midY + offset * rowH;
            boolean selected = offset == 0;
            if (selected) {
                graphics.fill(reelLeft + 2, y - 2, reelRight - 2, y + 12, 0x55FFD700);
            }
            int color = selected ? 0xFFFFD700 : 0xFF9A9A9A;
            graphics.centeredText(this.font, Component.literal(text), cx, y, color);
        }

        Component status = ticksRemaining > 0
                ? Component.translatable("letterrush.roulette.spinning")
                : Component.translatable("letterrush.roulette.winner", labels.get(Math.floorMod(winnerIndex, labels.size())));
        graphics.centeredText(this.font, status, cx, top + PANEL_H - 20, 0xFFFFFFFF);
    }

    private int displayIndex() {
        if (ticksRemaining <= 5 || holdTicks >= 0) {
            return winnerIndex;
        }
        int elapsed = Math.max(1, ticksRemaining);
        return Math.floorMod(winnerIndex + elapsed * 3, labels.size());
    }
}
