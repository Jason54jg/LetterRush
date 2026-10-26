package io.github.jason54.letterrush.fabric;

import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.LetterRushRuntime;
import io.github.jason54.letterrush.network.RoulettePayload;
import io.github.jason54.letterrush.reward.DatapackRewardLoader;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;

public final class LetterRushFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        LetterRushRuntime.setConfigDirectory(FabricLoader.getInstance().getConfigDir());

        PayloadTypeRegistry.clientboundPlay().register(RoulettePayload.ID, RoulettePayload.CODEC);
        LetterRushRuntime.setRouletteBroadcaster((server, payload) -> {
            for (var player : server.getPlayerList().getPlayers()) {
                ServerPlayNetworking.send(player, payload);
            }
        });

        ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.fromNamespaceAndPath(LetterRush.MOD_ID, "rewards");
            }

            @Override
            public void onResourceManagerReload(ResourceManager resourceManager) {
                DatapackRewardLoader.reload(resourceManager);
            }
        });

        ServerLifecycleEvents.SERVER_STARTING.register(LetterRush::onServerStarting);
        ServerLifecycleEvents.SERVER_STOPPED.register(LetterRush::onServerStopped);
        ServerTickEvents.END_SERVER_TICK.register(LetterRush::onServerTick);
        CommandRegistrationCallback.EVENT.register((dispatcher, registry, selection) ->
                LetterRush.registerCommands(dispatcher));

        LetterRush.LOGGER.info("LetterRush initialized (Fabric / MC 26.x).");
    }
}
