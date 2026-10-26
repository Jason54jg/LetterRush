package io.github.jason54.letterrush.fabric;

import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.client.RouletteScreen;
import io.github.jason54.letterrush.network.RoulettePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class LetterRushFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(
                RoulettePayload.ID,
                (payload, context) -> context.client().execute(() -> {
                    LetterRush.LOGGER.info(
                            "Opening roulette GUI ({} rewards, spin {} ticks).",
                            payload.labels().size(),
                            payload.spinTicks()
                    );
                    RouletteScreen.open(payload);
                })
        );
        LetterRush.LOGGER.info("LetterRush client initialized.");
    }
}
