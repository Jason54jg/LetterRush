package io.github.jason54.letterrush.game;

import io.github.jason54.letterrush.util.ServerPlayerAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;

/**
 * Shared entry for platform pickup events (real item-entity pickups only).
 */
public final class PickupHooks {
    private PickupHooks() {
    }

    public static void onPickup(ServerPlayer player, ItemStack stack) {
        var server = ServerPlayerAccess.server(player);
        if (server == null || stack.isEmpty()) {
            return;
        }
        GameManager.get(server).onPlayerPickup(player, stack.copy());
    }
}
