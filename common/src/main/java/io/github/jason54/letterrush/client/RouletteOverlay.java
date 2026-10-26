package io.github.jason54.letterrush.client;

import io.github.jason54.letterrush.network.RoulettePayload;

/**
 * Opens {@link RouletteScreen} when the server sends a roulette payload.
 * Kept as a stable entry point for Architectury networking.
 */
public final class RouletteOverlay {
    private RouletteOverlay() {
    }

    public static void start(RoulettePayload payload) {
        RouletteScreen.open(payload);
    }
}
