package io.github.jason54.letterrush.fabric;

import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.client.RouletteOverlay;
import io.github.jason54.letterrush.network.RoulettePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

/**
 * Fabric client for MC 26.1 / 26.1.1 only: Fabric API HUD hooks still target removed {@code GuiGraphics},
 * so the roulette overlay is not registered here (payload + tick state still work).
 */
public final class LetterRushFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                RoulettePayload.ID,
                (payload, context) -> RouletteOverlay.start(payload)
        );
        ClientTickEvents.END_CLIENT_TICK.register(client -> RouletteOverlay.clientTick());
        LetterRush.LOGGER.warn("LetterRush client (26.1.x): roulette HUD disabled until Fabric API matches GuiGraphicsExtractor.");
        LetterRush.LOGGER.info("LetterRush client initialized.");
    }
}
