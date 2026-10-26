package io.github.jason54.letterrush.util;

import net.minecraft.server.command.ServerCommandSource;

final class CommandPermissionsImpl {
    private CommandPermissionsImpl() {
    }

    static boolean hasLevel(ServerCommandSource source, int level) {
        return source.hasPermissionLevel(level);
    }

    static ServerCommandSource withLevel(ServerCommandSource source, int level) {
        return source.withLevel(level);
    }
}
