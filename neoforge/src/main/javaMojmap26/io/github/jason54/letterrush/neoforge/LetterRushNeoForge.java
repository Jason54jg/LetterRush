package io.github.jason54.letterrush.neoforge;

import io.github.jason54.letterrush.LetterRush;
import io.github.jason54.letterrush.LetterRushRuntime;
import io.github.jason54.letterrush.client.RouletteOverlay;
import io.github.jason54.letterrush.game.PickupHooks;
import io.github.jason54.letterrush.network.RoulettePayload;
import io.github.jason54.letterrush.reward.DatapackRewardLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@Mod(LetterRush.MOD_ID)
public final class LetterRushNeoForge {
    public LetterRushNeoForge(IEventBus modBus) {
        LetterRushRuntime.setConfigDirectory(net.neoforged.fml.loading.FMLPaths.CONFIGDIR.get());
        LetterRushRuntime.setRouletteBroadcaster((server, payload) -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                PacketDistributor.sendToPlayer(player, payload);
            }
        });

        modBus.addListener(this::registerPayloads);
        modBus.addListener((FMLClientSetupEvent event) ->
                NeoForge.EVENT_BUS.register(new LetterRushNeoForgeClient()));
        NeoForge.EVENT_BUS.register(this);
        LetterRush.LOGGER.info("LetterRush initialized (NeoForge / MC 26.x).");
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(LetterRush.MOD_ID);
        registrar.playToClient(
                RoulettePayload.TYPE,
                RoulettePayload.STREAM_CODEC,
                (payload, context) -> context.enqueueWork(() -> RouletteOverlay.start(payload))
        );
    }

    @SubscribeEvent
    public void onReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(
                Identifier.fromNamespaceAndPath(LetterRush.MOD_ID, "rewards"),
                (ResourceManagerReloadListener) DatapackRewardLoader::reload
        );
    }

    @SubscribeEvent
    public void onCommands(RegisterCommandsEvent event) {
        LetterRush.registerCommands(event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LetterRush.onServerStarting(event.getServer());
    }

    @SubscribeEvent
    public void onServerStopped(ServerStoppedEvent event) {
        LetterRush.onServerStopped(event.getServer());
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        LetterRush.onServerTick(event.getServer());
    }

    @SubscribeEvent
    public void onItemPickup(ItemEntityPickupEvent.Post event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) {
            return;
        }
        PickupHooks.onPickup(player, pickedStack(event));
    }

    private static ItemStack pickedStack(ItemEntityPickupEvent.Post event) {
        ItemStack original = event.getOriginalStack();
        ItemStack remaining = event.getItemEntity().getItem();
        int pickedCount = original.getCount() - remaining.getCount();
        if (pickedCount <= 0 && remaining.isEmpty()) {
            pickedCount = original.getCount();
        }
        if (pickedCount <= 0) {
            return ItemStack.EMPTY;
        }
        return original.copyWithCount(pickedCount);
    }
}
