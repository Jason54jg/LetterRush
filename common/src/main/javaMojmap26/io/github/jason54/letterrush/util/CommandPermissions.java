package io.github.jason54.letterrush.util;

import net.minecraft.commands.CommandSourceStack;

public final class CommandPermissions {
    private CommandPermissions() {
    }

    public static boolean hasLevel(CommandSourceStack source, int level) {
        return CommandPermissionsImpl.hasLevel(source, level);
    }

    public static CommandSourceStack withLevel(CommandSourceStack source, int level) {
        return CommandPermissionsImpl.withLevel(source, level);
    }
}
