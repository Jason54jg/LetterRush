package io.github.jason54.letterrush.neoforge;

import io.github.jason54.letterrush.client.RouletteOverlay;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

/** Client-side Forge bus listeners (registered from {@link LetterRushNeoForge}). */
public final class LetterRushNeoForgeClient {
    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        RouletteOverlay.clientTick();
    }

    @SubscribeEvent
    public void onRenderGui(RenderGuiEvent.Post event) {
        RouletteOverlay.render(event.getGuiGraphics());
    }
}
