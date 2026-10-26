package io.github.jason54.letterrush.reward;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.util.CommandPermissions;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

/**
 * Applies the winning reward to a player on the logical server.
 */
public final class RewardExecutor {
    private RewardExecutor() {
    }

    public static void grant(MinecraftServer server, ServerPlayerEntity winner, RewardDefinition reward) {
        switch (reward.type) {
            case ITEM -> giveItem(winner, reward);
            case EXPERIENCE -> winner.addExperience(reward.amount);
            case COMMAND -> runCommand(server, winner, reward.command);
        }
    }

    private static void giveItem(ServerPlayerEntity player, RewardDefinition reward) {
        Identifier id = Identifier.tryParse(reward.item);
        if (id == null) {
            LetterRush.LOGGER.warn("Invalid reward item id: {}", reward.item);
            return;
        }
        Item item = Registries.ITEM.get(id);
        if (item == null) {
            LetterRush.LOGGER.warn("Unknown reward item: {}", reward.item);
            return;
        }
        ItemStack stack = new ItemStack(item, Math.max(1, reward.count));
        if (!player.giveItemStack(stack)) {
            player.dropItem(stack, false);
        }
    }

    private static void runCommand(MinecraftServer server, ServerPlayerEntity winner, String command) {
        if (command == null || command.isBlank()) {
            return;
        }
        String trimmed = command.stripLeading();
        if (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1);
        }
        ServerCommandSource source = CommandPermissions.withLevel(server.getCommandSource(), 4)
                .withSilent()
                .withEntity(winner)
                .withPosition(new Vec3d(winner.getX(), winner.getY(), winner.getZ()));
        try {
            var dispatcher = server.getCommandManager().getDispatcher();
            ParseResults<ServerCommandSource> parsed = dispatcher.parse(new StringReader(trimmed), source);
            server.getCommandManager().execute(parsed, trimmed);
        } catch (Exception e) {
            LetterRush.LOGGER.error("Failed to run reward command for {}", winner.getName().getString(), e);
        }
    }
}
