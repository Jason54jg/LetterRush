package io.github.jason54.letterrush.fabric.mixin;

import io.github.jason54.letterrush.game.PickupHooks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Detects real item-entity pickups on Fabric.
 * Full pickups restore the stack count after discard — use {@link net.minecraft.entity.Entity#isRemoved()}.
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityPickupMixin {
    @Unique
    private ItemStack letterrush$stackBefore = ItemStack.EMPTY;

    @Unique
    private int letterrush$countBefore;

    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
    private void letterrush$captureStack(PlayerEntity entity, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        ItemStack stack = self.getStack();
        letterrush$stackBefore = stack.copy();
        letterrush$countBefore = stack.getCount();
    }

    @Inject(method = "onPlayerCollision", at = @At("TAIL"))
    private void letterrush$afterCollision(PlayerEntity entity, CallbackInfo ci) {
        if (!(entity instanceof ServerPlayerEntity player)) {
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
        int remaining = self.isRemoved() ? 0 : self.getStack().getCount();
        int pickedCount = beforeCount - remaining;
        if (pickedCount <= 0) {
            return;
        }
        ItemStack picked = before.copy();
        picked.setCount(pickedCount);
        PickupHooks.onPickup(player, picked);
    }
}
