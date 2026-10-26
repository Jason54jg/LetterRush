package io.github.jason54.letterrush.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

/** Compiled for Minecraft 1.21.9 and newer. */
public final class ServerPlayerAccessImpl {
    private ServerPlayerAccessImpl() {
    }

    public static ServerWorld world(ServerPlayerEntity player) {
        return player.getEntityWorld();
    }

    public static MinecraftServer server(ServerPlayerEntity player) {
        return player.getEntityWorld().getServer();
    }
}
