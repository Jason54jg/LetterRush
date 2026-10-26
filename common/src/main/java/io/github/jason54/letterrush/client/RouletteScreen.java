package io.github.jason54.letterrush.client;

import io.github.jason54.letterrush.network.RoulettePayload;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

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
        super(Text.translatable("letterrush.roulette.title"));
        this.labels = List.copyOf(new ArrayList<>(payload.labels()));
        this.winnerIndex = payload.winnerIndex();
        this.winnerId = payload.winnerId();
        this.ticksRemaining = Math.max(10, payload.spinTicks());
    }

    public static void open(RoulettePayload payload) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.setScreen(new RouletteScreen(payload));
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
                close();
            }
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderInGameBackground(context);
        super.render(context, mouseX, mouseY, delta);
        if (labels.isEmpty()) {
            return;
        }

        int cx = this.width / 2;
        int cy = this.height / 2;
        int left = cx - PANEL_W / 2;
        int top = cy - PANEL_H / 2;

        context.fill(left, top, left + PANEL_W, top + PANEL_H, 0xF0161620);
        context.fill(left, top, left + PANEL_W, top + 1, 0xFFD4AF37);
        context.fill(left, top + PANEL_H - 1, left + PANEL_W, top + PANEL_H, 0xFFD4AF37);
        context.fill(left, top, left + 1, top + PANEL_H, 0xFFD4AF37);
        context.fill(left + PANEL_W - 1, top, left + PANEL_W, top + PANEL_H, 0xFFD4AF37);

        context.drawCenteredTextWithShadow(this.textRenderer, this.title, cx, top + 12, 0xFFFFD700);

        int index = displayIndex();
        int rowH = 16;
        int midY = cy - 5;
        int reelLeft = left + 20;
        int reelRight = left + PANEL_W - 20;
        context.fill(reelLeft, midY - VISIBLE_ROWS * rowH - 4, reelRight, midY + (VISIBLE_ROWS + 1) * rowH + 2, 0xAA000000);

        for (int offset = -VISIBLE_ROWS; offset <= VISIBLE_ROWS; offset++) {
            int i = Math.floorMod(index + offset, labels.size());
            String text = labels.get(i);
            int y = midY + offset * rowH;
            boolean selected = offset == 0;
            if (selected) {
                context.fill(reelLeft + 2, y - 2, reelRight - 2, y + 12, 0x55FFD700);
            }
            int color = selected ? 0xFFFFD700 : 0xFF9A9A9A;
            context.drawCenteredTextWithShadow(this.textRenderer, Text.literal(text), cx, y, color);
        }

        Text status = ticksRemaining > 0
                ? Text.translatable("letterrush.roulette.spinning")
                : Text.translatable("letterrush.roulette.winner", labels.get(Math.floorMod(winnerIndex, labels.size())));
        context.drawCenteredTextWithShadow(this.textRenderer, status, cx, top + PANEL_H - 20, 0xFFFFFFFF);
    }

    private int displayIndex() {
        if (ticksRemaining <= 5 || holdTicks >= 0) {
            return winnerIndex;
        }
        int elapsed = Math.max(1, ticksRemaining);
        return Math.floorMod(winnerIndex + elapsed * 3, labels.size());
    }
}
