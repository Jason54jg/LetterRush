package io.github.jason54.letterrush.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.github.jason54.letterrush.client.RouletteOverlay;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * S2C roulette packet. On Fabric API / Architectury 17+, {@code registerReceiver} already
 * registers the payload type — do not call {@code registerS2CPayloadType} on the client too.
 */
public final class LetterRushNetworking {
    private LetterRushNetworking() {
    }

    public static void initCommon() {
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.registerReceiver(
                    NetworkManager.Side.S2C,
                    RoulettePayload.ID,
                    RoulettePayload.CODEC,
                    (payload, context) -> context.queue(() -> RouletteOverlay.start(payload))
            );
        } else {
            NetworkManager.registerS2CPayloadType(RoulettePayload.ID, RoulettePayload.CODEC);
        }
    }

    public static void sendRoulette(MinecraftServer server, RoulettePayload payload) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            NetworkManager.sendToPlayer(player, payload);
        }
    }
}
