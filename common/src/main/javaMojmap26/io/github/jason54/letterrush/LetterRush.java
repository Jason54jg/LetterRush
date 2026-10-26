package io.github.jason54.letterrush;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.ReloadListenerRegistry;
import io.github.jason54.letterrush.command.LetterRushCommands;
import io.github.jason54.letterrush.config.LetterRushConfig;
import io.github.jason54.letterrush.game.EnglishItemNames;
import io.github.jason54.letterrush.game.GameManager;
import io.github.jason54.letterrush.network.LetterRushNetworking;
import io.github.jason54.letterrush.reward.DatapackRewardLoader;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Shared entry point for Fabric and NeoForge. */
public final class LetterRush {
    public static final String MOD_ID = "letterrush";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private LetterRush() {
    }

    public static void init() {
        LetterRushNetworking.initCommon();

        ReloadListenerRegistry.register(
                PackType.SERVER_DATA,
                DatapackRewardLoader::reload
        );

        LifecycleEvent.SERVER_STARTING.register(server -> {
            LetterRushConfig.load(server);
            EnglishItemNames.invalidateCache();
            GameManager.reset();
        });

        LifecycleEvent.SERVER_STOPPED.register(server -> GameManager.reset());

        TickEvent.SERVER_POST.register(server -> GameManager.get(server).tick(server));

        CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) ->
                LetterRushCommands.register(dispatcher));

        LOGGER.info("LetterRush initialized.");
    }
}
