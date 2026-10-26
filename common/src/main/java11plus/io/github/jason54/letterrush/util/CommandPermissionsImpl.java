package io.github.jason54.letterrush.util;

import net.minecraft.command.permission.LeveledPermissionPredicate;
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.server.command.ServerCommandSource;

final class CommandPermissionsImpl {
    private CommandPermissionsImpl() {
    }

    static boolean hasLevel(ServerCommandSource source, int level) {
        PermissionLevel permissionLevel = PermissionLevel.fromLevel(level);
        return source.getPermissions().hasPermission(new Permission.Level(permissionLevel));
    }

    static ServerCommandSource withLevel(ServerCommandSource source, int level) {
        PermissionLevel permissionLevel = PermissionLevel.fromLevel(level);
        return source.withPermissions(LeveledPermissionPredicate.fromLevel(permissionLevel));
    }
}
