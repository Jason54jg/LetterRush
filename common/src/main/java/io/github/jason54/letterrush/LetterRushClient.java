package io.github.jason54.letterrush;

import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.client.ClientGuiEvent;
import io.github.jason54.letterrush.client.RouletteOverlay;

public final class LetterRushClient {
    private LetterRushClient() {
    }

    public static void init() {
        ClientTickEvent.CLIENT_POST.register(instance -> RouletteOverlay.clientTick());
        ClientGuiEvent.RENDER_HUD.register((drawContext, tickDelta) -> RouletteOverlay.render(drawContext));
        LetterRush.LOGGER.info("LetterRush client initialized.");
    }
}
