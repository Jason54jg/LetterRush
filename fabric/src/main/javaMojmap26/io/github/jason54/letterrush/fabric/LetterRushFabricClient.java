package io.github.jason54.letterrush.fabric;

import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import io.github.jason54.letterrush.LetterRush;
import dev.architectury.networking.NetworkManager;
import io.github.jason54.letterrush.client.RouletteOverlay;
import io.github.jason54.letterrush.network.RoulettePayload;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.GuiGraphics;

public final class LetterRushFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        NetworkManager.registerReceiver(
                NetworkManager.Side.S2C,
                RoulettePayload.ID,
                RoulettePayload.CODEC,
                (payload, context) -> context.queue(() -> RouletteOverlay.start(payload))
        );
        ClientTickEvent.CLIENT_POST.register(client -> RouletteOverlay.clientTick());
        ClientGuiEvent.RENDER_HUD.register((GuiGraphics graphics, float tickDelta) -> RouletteOverlay.render(graphics));
        LetterRush.LOGGER.info("LetterRush client initialized.");
    }
}
