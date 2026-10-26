package io.github.jason54.letterrush.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class LetterRushNetworking {
    private LetterRushNetworking() {
    }

    public static void initCommon() {
        NetworkManager.registerS2CPayloadType(RoulettePayload.ID, RoulettePayload.CODEC);
    }

    public static void sendRoulette(MinecraftServer server, RoulettePayload payload) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            NetworkManager.sendToPlayer(player, payload);
        }
    }
}
