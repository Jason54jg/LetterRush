package io.github.jason54.letterrush.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

final class ServerPlayerAccessImpl {
    private ServerPlayerAccessImpl() {
    }

    static ServerLevel world(ServerPlayer player) {
        return player.level();
    }

    static MinecraftServer server(ServerPlayer player) {
        return player.level().getServer();
    }
}
