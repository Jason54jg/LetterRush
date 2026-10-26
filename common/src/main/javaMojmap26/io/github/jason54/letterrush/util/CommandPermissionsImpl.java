package io.github.jason54.letterrush.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.PermissionLevel;

final class CommandPermissionsImpl {
    private CommandPermissionsImpl() {
    }

    static boolean hasLevel(CommandSourceStack source, int level) {
        return Commands.hasPermission(permissionCheck(level)).test(source);
    }

    static CommandSourceStack withLevel(CommandSourceStack source, int level) {
        PermissionLevel permissionLevel = PermissionLevel.byId(Math.max(0, Math.min(4, level)));
        return source.withPermission(LevelBasedPermissionSet.forLevel(permissionLevel));
    }

    private static PermissionCheck permissionCheck(int level) {
        return switch (Math.max(0, Math.min(4, level))) {
            case 0 -> Commands.LEVEL_ALL;
            case 1 -> Commands.LEVEL_MODERATORS;
            case 3 -> Commands.LEVEL_ADMINS;
            case 4 -> Commands.LEVEL_OWNERS;
            default -> Commands.LEVEL_GAMEMASTERS;
        };
    }
}
