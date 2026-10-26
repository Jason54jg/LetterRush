package io.github.jason54.letterrush.network;

import io.github.jason54.letterrush.LetterRushRuntime;
import net.minecraft.server.MinecraftServer;

public final class LetterRushNetworking {
    private LetterRushNetworking() {
    }

    public static void sendRoulette(MinecraftServer server, RoulettePayload payload) {
        LetterRushRuntime.sendRoulette(server, payload);
    }
}
