package io.github.jason54.letterrush.fabric.mixin;

import io.github.jason54.letterrush.game.PickupHooks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Detects real item-entity pickups.
 * <p>
 * After a full pickup, vanilla restores the pre-pickup stack count for stats, so a
 * naive before/after count diff always looks like zero. Treat a discarded entity as
 * remaining = 0.
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityPickupMixin {
    @Unique
    private ItemStack letterrush$stackBefore = ItemStack.EMPTY;

    @Unique
    private int letterrush$countBefore;

    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void letterrush$captureStack(Player player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        ItemStack stack = self.getItem();
        letterrush$stackBefore = stack.copy();
        letterrush$countBefore = stack.getCount();
    }

    @Inject(method = "playerTouch", at = @At("TAIL"))
    private void letterrush$afterTouch(Player entity, CallbackInfo ci) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
        ItemStack before = letterrush$stackBefore;
        int beforeCount = letterrush$countBefore;
        letterrush$stackBefore = ItemStack.EMPTY;
        letterrush$countBefore = 0;
        if (before.isEmpty() || beforeCount <= 0) {
            return;
        }
        ItemEntity self = (ItemEntity) (Object) this;
        int remaining = self.isRemoved() ? 0 : self.getItem().getCount();
        int pickedCount = beforeCount - remaining;
        if (pickedCount <= 0) {
            return;
        }
        ItemStack picked = before.copy();
        picked.setCount(pickedCount);
        PickupHooks.onPickup(player, picked);
    }
}
