package io.github.jason54.letterrush.neoforge;

import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.LetterRushClient;
import io.github.jason54.letterrush.game.PickupHooks;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

@Mod(LetterRush.MOD_ID)
public final class LetterRushNeoForge {
    public LetterRushNeoForge() {
        LetterRush.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            LetterRushClient.init();
        }
        NeoForge.EVENT_BUS.register(this);
    }

    /**
     * Checks the collected portion of a completed server-player pickup for a round win.
     * Events for other players are ignored.
     */
    @SubscribeEvent
    public void onItemPickup(ItemEntityPickupEvent.Post event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity player)) {
            return;
        }
        PickupHooks.onPickup(player, pickedStack(event));
    }

    /**
     * Returns a copy of the original stack with the collected count, without changing the event stacks.
     * If the count difference is nonpositive and the remaining stack is empty, uses the original count.
     *
     * @return the collected stack, or an empty stack if no positive count is determined
     */
    private static ItemStack pickedStack(ItemEntityPickupEvent.Post event) {
        ItemStack original = event.getOriginalStack();
        ItemStack remaining = event.getItemEntity().getStack();
        int pickedCount = original.getCount() - remaining.getCount();
        if (pickedCount <= 0 && remaining.isEmpty()) {
            pickedCount = original.getCount();
        }
        if (pickedCount <= 0) {
            return ItemStack.EMPTY;
        }
        return original.copyWithCount(pickedCount);
    }
}
