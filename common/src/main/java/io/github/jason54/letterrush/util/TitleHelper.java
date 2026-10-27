package io.github.jason54.letterrush.util;

import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public final class TitleHelper {
    private TitleHelper() {
    }

    public static void showRoundStart(MinecraftServer server, char letter) {
        Text title = LetterRushText.message("title_letter", "letterrush.title.letter", String.valueOf(letter));
        Text subtitle = LetterRushText.message("title_subtitle", "letterrush.title.subtitle");
        forEachPlayer(server, player -> sendTitle(player, title, subtitle));
    }

    public static void showWinner(MinecraftServer server, String playerName, String rewardLabel) {
        Text title = LetterRushText.message("title_winner", "letterrush.title.winner", playerName);
        Text subtitle = LetterRushText.message("title_reward", "letterrush.title.reward", rewardLabel);
        forEachPlayer(server, player -> sendTitle(player, title, subtitle));
    }

    public static void showTimeUp(MinecraftServer server) {
        Text title = Text.translatable("letterrush.bossbar.time_up");
        forEachPlayer(server, player -> sendTitle(player, title, Text.empty()));
    }

    private static void sendTitle(ServerPlayerEntity player, Text title, Text subtitle) {
        // Use send(packet, null): sendPacket() remaps incorrectly on NeoForge 1.21.11 (NoSuchMethodError).
        player.networkHandler.send(new TitleFadeS2CPacket(10, 60, 20), null);
        player.networkHandler.send(new TitleS2CPacket(title), null);
        player.networkHandler.send(new SubtitleS2CPacket(subtitle), null);
    }

    private static void forEachPlayer(MinecraftServer server, java.util.function.Consumer<ServerPlayerEntity> action) {
        server.getPlayerManager().getPlayerList().forEach(action);
    }
}
