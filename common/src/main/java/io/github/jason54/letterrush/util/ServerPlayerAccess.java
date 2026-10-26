package io.github.jason54.letterrush.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/**
 * Minecraft 1.21.1–1.21.8 and 1.21.9+ expose different ServerPlayer accessors; impl is versioned at compile time.
 */
public final class ServerPlayerAccess {
    private ServerPlayerAccess() {
    }

    public static ServerWorld world(ServerPlayerEntity player) {
        return ServerPlayerAccessImpl.world(player);
    }

    public static MinecraftServer server(ServerPlayerEntity player) {
        return ServerPlayerAccessImpl.server(player);
    }
}
