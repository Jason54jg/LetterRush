package io.github.jason54.letterrush.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/** Compiled for Minecraft 1.21.1 through 1.21.8. */
public final class ServerPlayerAccessImpl {
    private ServerPlayerAccessImpl() {
    }

    public static ServerWorld world(ServerPlayerEntity player) {
        return (ServerWorld) player.getWorld();
    }

    public static MinecraftServer server(ServerPlayerEntity player) {
        return player.getServer();
    }
}
