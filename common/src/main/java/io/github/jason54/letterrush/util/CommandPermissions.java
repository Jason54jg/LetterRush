package io.github.jason54.letterrush.util;

import net.minecraft.server.command.ServerCommandSource;

/** Command permission checks differ on Minecraft 1.21.11+; impl is selected at compile time. */
public final class CommandPermissions {
    private CommandPermissions() {
    }

    public static boolean hasLevel(ServerCommandSource source, int level) {
        return CommandPermissionsImpl.hasLevel(source, level);
    }

    public static ServerCommandSource withLevel(ServerCommandSource source, int level) {
        return CommandPermissionsImpl.withLevel(source, level);
    }
}
