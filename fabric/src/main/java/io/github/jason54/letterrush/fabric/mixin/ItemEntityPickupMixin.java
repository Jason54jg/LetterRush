package io.github.jason54.letterrush.fabric.mixin;

import io.github.jason54.letterrush.game.PickupHooks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Detects real item-entity pickups on Fabric (no stable public callback in 1.21.1).
 */
@Mixin(ItemEntity.class)
public abstract class ItemEntityPickupMixin {
    @Unique
    private ItemStack letterrush$stackBeforeCollision = ItemStack.EMPTY;

    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
    private void letterrush$captureStack(Entity entity, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        letterrush$stackBeforeCollision = self.getStack().copy();
    }

    @Inject(method = "onPlayerCollision", at = @At("TAIL"))
    private void letterrush$afterCollision(Entity entity, CallbackInfo ci) {
        if (!(entity instanceof ServerPlayerEntity player)) {
            return;
        }
        ItemEntity self = (ItemEntity) (Object) this;
        ItemStack after = self.getStack();
        if (letterrush$stackBeforeCollision.isEmpty()) {
            return;
        }
        int pickedCount = letterrush$stackBeforeCollision.getCount() - after.getCount();
        if (pickedCount <= 0) {
            return;
        }
        ItemStack picked = letterrush$stackBeforeCollision.copy();
        picked.setCount(pickedCount);
        PickupHooks.onPickup(player, picked);
    }
}
