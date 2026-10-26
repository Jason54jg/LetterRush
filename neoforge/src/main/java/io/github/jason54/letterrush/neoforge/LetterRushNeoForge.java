package io.github.jason54.letterrush.neoforge;

import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.LetterRushClient;
import io.github.jason54.letterrush.game.PickupHooks;
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

    @SubscribeEvent
    public void onItemPickup(ItemEntityPickupEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayerEntity player)) {
            return;
        }
        PickupHooks.onPickup(player, event.getItemEntity().getStack());
    }
}
