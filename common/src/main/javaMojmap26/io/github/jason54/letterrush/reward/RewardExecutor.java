package io.github.jason54.letterrush.reward;

import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.util.CommandPermissions;
import io.github.jason54.letterrush.util.PlayerDropHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;

/**
 * Applies the winning reward to a player on the logical server.
 */
public final class RewardExecutor {
    private RewardExecutor() {
    }

    public static void grant(MinecraftServer server, ServerPlayer winner, RewardDefinition reward) {
        switch (reward.type) {
            case ITEM -> giveItem(winner, reward);
            case EXPERIENCE -> winner.giveExperiencePoints(reward.amount);
            case COMMAND -> runCommand(server, winner, reward.command);
        }
    }

    private static void giveItem(ServerPlayer player, RewardDefinition reward) {
        Identifier id = Identifier.tryParse(reward.item);
        if (id == null) {
            LetterRush.LOGGER.warn("Invalid reward item id: {}", reward.item);
            return;
        }
        if (!BuiltInRegistries.ITEM.containsKey(id)) {
            LetterRush.LOGGER.warn("Unknown reward item: {}", reward.item);
            return;
        }
        Item item = BuiltInRegistries.ITEM.getValue(id);
        if (item == Items.AIR) {
            LetterRush.LOGGER.warn("Unknown reward item: {}", reward.item);
            return;
        }
        ItemStack stack = new ItemStack(item, Math.max(1, reward.count));
        if (!player.getInventory().add(stack)) {
            PlayerDropHelper.dropOverflow(player, stack);
        }
    }

    private static void runCommand(MinecraftServer server, ServerPlayer winner, String command) {
        if (command == null || command.isBlank()) {
            return;
        }
        String trimmed = command.stripLeading();
        if (trimmed.startsWith("/")) {
            trimmed = trimmed.substring(1);
        }
        CommandSourceStack source = CommandPermissions.withLevel(server.createCommandSourceStack(), 4)
                .withSuppressedOutput()
                .withEntity(winner)
                .withPosition(new Vec3(winner.getX(), winner.getY(), winner.getZ()));
        try {
            var dispatcher = server.getCommands().getDispatcher();
            ParseResults<CommandSourceStack> parsed = dispatcher.parse(new StringReader(trimmed), source);
            server.getCommands().performCommand(parsed, trimmed);
        } catch (Exception e) {
            LetterRush.LOGGER.error("Failed to run reward command for {}", winner.getName().getString(), e);
        }
    }
}
