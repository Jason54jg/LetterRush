package io.github.jason54.letterrush.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Prediction;
import net.minecraft.world.item.ItemStack;

public final class PlayerDropHelper {
    private PlayerDropHelper() {
    }

    public static void dropOverflow(ServerPlayer player, ItemStack stack) {
        player.drop(stack, false, Prediction.SERVER_ONLY);
    }
}
