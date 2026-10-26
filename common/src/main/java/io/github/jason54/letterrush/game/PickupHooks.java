package io.github.jason54.letterrush.game;

import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

/**
 * Shared entry for platform pickup events (real item-entity pickups only).
 */
public final class PickupHooks {
    private PickupHooks() {
    }

    public static void onPickup(ServerPlayerEntity player, ItemStack stack) {
        if (player.getServer() == null || stack.isEmpty()) {
            return;
        }
        GameManager.get(player.getServer()).onPlayerPickup(player, stack.copy());
    }
}
