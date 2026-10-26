package io.github.jason54.letterrush.fabric;

import io.github.jason54.letterrush.LetterRush;
import net.fabricmc.api.ModInitializer;

public final class LetterRushFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LetterRush.init();
    }
}
