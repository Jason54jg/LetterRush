package io.github.jason54.letterrush;

import com.mojang.brigadier.CommandDispatcher;
import io.github.jason54.letterrush.command.LetterRushCommands;
import io.github.jason54.letterrush.config.LetterRushConfig;
import io.github.jason54.letterrush.game.EnglishItemNames;
import io.github.jason54.letterrush.game.GameManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Shared lifecycle; platform entrypoints wire events and call these methods. */
public final class LetterRush {
    public static final String MOD_ID = "letterrush";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private LetterRush() {
    }

    public static void onServerStarting(MinecraftServer server) {
        LetterRushConfig.load(server);
        EnglishItemNames.invalidateCache();
        GameManager.reset();
    }

    public static void onServerStopped(MinecraftServer server) {
        GameManager.reset();
    }

    public static void onServerTick(MinecraftServer server) {
        GameManager.get(server).tick(server);
    }

    public static void registerCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        LetterRushCommands.register(dispatcher);
    }
}
