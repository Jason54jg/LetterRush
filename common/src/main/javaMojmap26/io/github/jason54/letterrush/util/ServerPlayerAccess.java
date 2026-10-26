package io.github.jason54.letterrush.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class ServerPlayerAccess {
    private ServerPlayerAccess() {
    }

    public static ServerLevel world(ServerPlayer player) {
        return ServerPlayerAccessImpl.world(player);
    }

    public static MinecraftServer server(ServerPlayer player) {
        return ServerPlayerAccessImpl.server(player);
    }
}
