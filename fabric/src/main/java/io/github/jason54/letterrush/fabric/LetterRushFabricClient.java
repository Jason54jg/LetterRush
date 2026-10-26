package io.github.jason54.letterrush.fabric;

import io.github.jason54.letterrush.LetterRushClient;
import net.fabricmc.api.ClientModInitializer;

public final class LetterRushFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        LetterRushClient.init();
    }
}
