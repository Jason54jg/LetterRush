package io.github.jason54.letterrush.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.config.LetterRushConfig;
import io.github.jason54.letterrush.game.GameManager;
import io.github.jason54.letterrush.game.GameSession;
import io.github.jason54.letterrush.util.LetterRushText;

import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.ChatFormatting;

public final class LetterRushCommands {
    private LetterRushCommands() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("letterrush")
                        .requires(Commands.hasPermission(permissionCheck(LetterRushConfig.get().permissionLevel)))
                        .then(Commands.literal("start")
                                .executes(LetterRushCommands::start))
                        .then(Commands.literal("stop")
                                .executes(LetterRushCommands::stop))
                        .then(Commands.literal("status")
                                .executes(LetterRushCommands::status))
                        .then(Commands.literal("reload")
                                .executes(LetterRushCommands::reload))
        );
        LetterRush.LOGGER.info("Registered /letterrush (permission_level={})", LetterRushConfig.get().permissionLevel);
    }

    private static net.minecraft.server.permissions.PermissionCheck permissionCheck(int level) {
        return switch (Math.max(0, Math.min(4, level))) {
            case 0 -> Commands.LEVEL_ALL;
            case 1 -> Commands.LEVEL_MODERATORS;
            case 3 -> Commands.LEVEL_ADMINS;
            case 4 -> Commands.LEVEL_OWNERS;
            default -> Commands.LEVEL_GAMEMASTERS;
        };
    }

    private static int start(CommandContext<CommandSourceStack> ctx) {
        CommandSourceStack source = ctx.getSource();
        GameManager manager = GameManager.get(source.getServer());
        if (manager.isRunning()) {
            source.sendSuccess(() -> LetterRushText.message("already_running", "letterrush.chat.already_running")
                    .withStyle(ChatFormatting.RED), true);
            return 0;
        }
        try {
            manager.start(source.getServer());
        } catch (RuntimeException e) {
            LetterRush.LOGGER.error("Failed to start LetterRush", e);
            throw e;
        }
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
        GameSession session = manager.session();
        source.sendSuccess(() -> LetterRushText.message(
                "status",
                "letterrush.chat.status",
                String.valueOf(session.letter()),
                GameSession.formatElapsed(session.secondsElapsed())
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
