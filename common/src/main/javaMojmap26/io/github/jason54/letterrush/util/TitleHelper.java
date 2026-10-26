package io.github.jason54.letterrush.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class TitleHelper {
    private TitleHelper() {
    }

    public static void showRoundStart(MinecraftServer server, char letter) {
        Component title = LetterRushText.message("title_letter", "letterrush.title.letter", String.valueOf(letter));
        Component subtitle = LetterRushText.message("title_subtitle", "letterrush.title.subtitle");
        forEachPlayer(server, player -> sendTitle(player, title, subtitle));
    }

    public static void showWinner(MinecraftServer server, String playerName, String rewardLabel) {
        Component title = LetterRushText.message("title_winner", "letterrush.title.winner", playerName);
        Component subtitle = LetterRushText.message("title_reward", "letterrush.title.reward", rewardLabel);
        forEachPlayer(server, player -> sendTitle(player, title, subtitle));
    }

    public static void showTimeUp(MinecraftServer server) {
        Component title = Component.translatable("letterrush.bossbar.time_up");
        forEachPlayer(server, player -> sendTitle(player, title, Component.empty()));
    }

    private static void sendTitle(ServerPlayer player, Component title, Component subtitle) {
        player.connection.send(new ClientboundSetTitlesAnimationPacket(10, 60, 20));
        player.connection.send(new ClientboundSetTitleTextPacket(title));
        player.connection.send(new ClientboundSetSubtitleTextPacket(subtitle));
    }

    private static void forEachPlayer(MinecraftServer server, java.util.function.Consumer<ServerPlayer> action) {
        server.getPlayerList().getPlayers().forEach(action);
    }
}
