package io.github.jason54.letterrush;

import io.github.jason54.letterrush.network.RoulettePayload;
import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

/** Platform hooks (config path, S2C roulette packets). Set from Fabric / NeoForge entrypoints. */
public final class LetterRushRuntime {
    @FunctionalInterface
    public interface RouletteBroadcaster {
        void send(MinecraftServer server, RoulettePayload payload);
    }

    private static Path configDirectory = Path.of("config");
    private static RouletteBroadcaster rouletteBroadcaster = (server, payload) -> {};

    private LetterRushRuntime() {
    }

    public static void setConfigDirectory(Path directory) {
        configDirectory = directory;
    }

    public static Path configDirectory() {
        return configDirectory;
    }

    public static void setRouletteBroadcaster(RouletteBroadcaster broadcaster) {
        rouletteBroadcaster = broadcaster;
    }

    public static void sendRoulette(MinecraftServer server, RoulettePayload payload) {
        rouletteBroadcaster.send(server, payload);
    }
}
