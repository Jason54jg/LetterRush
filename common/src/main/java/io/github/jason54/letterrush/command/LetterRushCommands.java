package io.github.jason54.letterrush.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.config.LetterRushConfig;
import io.github.jason54.letterrush.game.GameManager;
import io.github.jason54.letterrush.game.GameSession;
import io.github.jason54.letterrush.util.CommandPermissions;
import io.github.jason54.letterrush.util.LetterRushText;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.Formatting;

public final class LetterRushCommands {
    private LetterRushCommands() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
                CommandManager.literal("letterrush")
                        .requires(LetterRushCommands::hasPermission)
                        .then(CommandManager.literal("start")
                                .executes(LetterRushCommands::start))
                        .then(CommandManager.literal("stop")
                                .executes(LetterRushCommands::stop))
                        .then(CommandManager.literal("status")
                                .executes(LetterRushCommands::status))
                        .then(CommandManager.literal("reload")
                                .executes(LetterRushCommands::reload))
        );
        LetterRush.LOGGER.info("Registered /letterrush (permission_level={})", LetterRushConfig.get().permissionLevel);
    }

    private static boolean hasPermission(ServerCommandSource source) {
        return CommandPermissions.hasLevel(source, LetterRushConfig.get().permissionLevel);
    }

    private static int start(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        GameManager manager = GameManager.get(source.getServer());
        if (manager.isRunning()) {
            source.sendFeedback(() -> LetterRushText.message("already_running", "letterrush.chat.already_running")
                    .formatted(Formatting.RED), true);
            return 0;
        }
        if (!manager.start(source.getServer())) {
            source.sendFeedback(() -> LetterRushText.message("start_failed", "letterrush.chat.start_failed")
                    .formatted(Formatting.RED), true);
            return 0;
        }
        return 1;
    }

    private static int stop(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        GameManager manager = GameManager.get(source.getServer());
        if (!manager.isRunning()) {
            source.sendFeedback(() -> LetterRushText.message("not_running", "letterrush.chat.not_running")
                    .formatted(Formatting.RED), true);
            return 0;
        }
        manager.stop(source.getServer(), true);
        source.sendFeedback(() -> LetterRushText.message("stopped", "letterrush.chat.stopped")
                .formatted(Formatting.YELLOW), true);
        return 1;
    }

    private static int status(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        GameManager manager = GameManager.get(source.getServer());
        if (!manager.isRunning()) {
            source.sendFeedback(() -> LetterRushText.message("not_running", "letterrush.chat.not_running")
                    .formatted(Formatting.GRAY), false);
            return 0;
        }
        GameSession session = manager.session();
        source.sendFeedback(() -> LetterRushText.message(
                "status",
                "letterrush.chat.status",
                String.valueOf(session.letter()),
                GameSession.formatElapsed(session.secondsElapsed())
        ).formatted(Formatting.AQUA), false);
        return 1;
    }

    private static int reload(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        LetterRushConfig.reload(source.getServer());
        source.sendFeedback(() -> LetterRushText.message("reloaded", "letterrush.chat.reloaded")
                .formatted(Formatting.GREEN), true);
        return 1;
    }
}
