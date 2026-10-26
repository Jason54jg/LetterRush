package io.github.jason54.letterrush.game;

import io.github.jason54.letterrush.util.ServerPlayerAccess;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Shared entry for platform pickup events (real item-entity pickups only).
 */
public final class PickupHooks {
    private PickupHooks() {
    }

    public static void onPickup(ServerPlayerEntity player, ItemStack stack) {
        var server = ServerPlayerAccess.server(player);
        if (server == null || stack.isEmpty()) {
            return;
        }
        GameManager.get(server).onPlayerPickup(player, stack.copy());
    }
}
