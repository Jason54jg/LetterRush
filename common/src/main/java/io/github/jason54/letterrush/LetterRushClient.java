package io.github.jason54.letterrush;

public final class LetterRushClient {
    private LetterRushClient() {
    }

    public static void init() {
        // Roulette opens as RouletteScreen via LetterRushNetworking S2C receiver.
        LetterRush.LOGGER.info("LetterRush client initialized.");
    }
}
