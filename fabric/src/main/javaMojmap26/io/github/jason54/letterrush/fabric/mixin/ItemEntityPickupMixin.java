package io.github.jason54.letterrush.fabric.mixin;

import io.github.jason54.letterrush.game.PickupHooks;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityPickupMixin {
    @Unique
    private ItemStack letterrush$stackBeforeCollision = ItemStack.EMPTY;

    @Inject(method = "playerTouch", at = @At("HEAD"))
    private void letterrush$captureStack(Entity entity, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        letterrush$stackBeforeCollision = self.getItem().copy();
    }

    @Inject(method = "playerTouch", at = @At("TAIL"))
    private void letterrush$afterTouch(Entity entity, CallbackInfo ci) {
        if (!(entity instanceof ServerPlayer player)) {
            return;
        }
        ItemEntity self = (ItemEntity) (Object) this;
        ItemStack after = self.getItem();
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
