package io.github.jason54.letterrush.fabric;

import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.client.RouletteOverlay;
import io.github.jason54.letterrush.network.RoulettePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.resources.Identifier;

public final class LetterRushFabricClient implements ClientModInitializer {
    private static final Identifier ROULETTE_HUD = Identifier.fromNamespaceAndPath(LetterRush.MOD_ID, "roulette");

    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                RoulettePayload.ID,
                (payload, context) -> RouletteOverlay.start(payload)
        );
        ClientTickEvents.END_CLIENT_TICK.register(client -> RouletteOverlay.clientTick());
        HudElementRegistry.addLast(ROULETTE_HUD, (graphics, tickDelta) -> RouletteOverlay.render(graphics));
        LetterRush.LOGGER.info("LetterRush client initialized.");
    }
}
