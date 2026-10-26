package io.github.jason54.letterrush.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.jason54.letterrush.config.LetterRushConfig;
import io.github.jason54.letterrush.game.GameManager;
import io.github.jason54.letterrush.util.CommandPermissions;
import io.github.jason54.letterrush.util.LetterRushText;

import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;

public final class LetterRushCommands {
    private LetterRushCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("letterrush")
                        .requires(LetterRushCommands::hasPermission)
                        .then(Commands.literal("start")
                                .executes(ctx -> start(ctx, LetterRushConfig.get().defaultDurationSeconds))
                                .then(Commands.argument("duration", IntegerArgumentType.integer(5, 3600))
                                        .executes(ctx -> start(ctx, IntegerArgumentType.getInteger(ctx, "duration")))))
                        .then(Commands.literal("stop")
                                .executes(LetterRushCommands::stop))
                        .then(Commands.literal("status")
                                .executes(LetterRushCommands::status))
                        .then(Commands.literal("reload")
                                .executes(LetterRushCommands::reload))
        );
    }

    private static boolean hasPermission(CommandSourceStack source) {
        return CommandPermissions.hasLevel(source, LetterRushConfig.get().permissionLevel);
    }

    private static int start(CommandContext<CommandSourceStack> ctx, int duration) {
        CommandSourceStack source = ctx.getSource();
        GameManager manager = GameManager.get(source.getServer());
        if (manager.isRunning()) {
            source.sendSuccess(() -> LetterRushText.message("already_running", "letterrush.chat.already_running")
                    .withStyle(ChatFormatting.RED), true);
            return 0;
        }
        manager.start(source.getServer(), duration);
        return 1;
    }

    private static int stop(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        GameManager manager = GameManager.get(source.getServer());
        if (!manager.isRunning()) {
            source.sendSuccess(() -> LetterRushText.message("not_running", "letterrush.chat.not_running")
                    .withStyle(ChatFormatting.RED), true);
            return 0;
        }
        manager.stop(source.getServer(), true);
        source.sendSuccess(() -> LetterRushText.message("stopped", "letterrush.chat.stopped")
                .withStyle(ChatFormatting.YELLOW), true);
        return 1;
    }

    private static int status(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        GameManager manager = GameManager.get(source.getServer());
        if (!manager.isRunning()) {
            source.sendSuccess(() -> LetterRushText.message("not_running", "letterrush.chat.not_running")
                    .withStyle(ChatFormatting.GRAY), false);
            return 0;
        }
        var session = manager.session();
        source.sendSuccess(() -> LetterRushText.message(
                "status",
                "letterrush.chat.status",
                String.valueOf(session.letter()),
                session.secondsRemaining()
        ).withStyle(ChatFormatting.AQUA), false);
        return 1;
    }

    private static int reload(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        LetterRushConfig.reload(source.getServer());
        source.sendSuccess(() -> LetterRushText.message("reloaded", "letterrush.chat.reloaded")
                .withStyle(ChatFormatting.GREEN), true);
        return 1;
    }
}
