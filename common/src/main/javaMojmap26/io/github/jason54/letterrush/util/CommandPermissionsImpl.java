package io.github.jason54.letterrush.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;

final class CommandPermissionsImpl {
    private CommandPermissionsImpl() {
    }

    static boolean hasLevel(CommandSourceStack source, int level) {
        PermissionLevel permissionLevel = PermissionLevel.byId(level);
        return source.permissions().hasPermission(new Permission.HasCommandLevel(permissionLevel));
    }

    static CommandSourceStack withLevel(CommandSourceStack source, int level) {
        PermissionLevel permissionLevel = PermissionLevel.byId(level);
        return source.withPermission(LevelBasedPermissionSet.forLevel(permissionLevel));
    }
}
