package io.github.jason54.letterrush.network;

import com.mojang.datafixers.util.Function4;
import io.github.jason54.letterrush.LetterRush;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/** Server → client payload that drives the optional roulette overlay. */
public record RoulettePayload(UUID winnerId, List<String> labels, int winnerIndex, int spinTicks) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<RoulettePayload> ID =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(LetterRush.MOD_ID, "roulette"));

    /** NeoForge payload registration alias. */
    public static final CustomPacketPayload.Type<RoulettePayload> TYPE = ID;

    public static final StreamCodec<RegistryFriendlyByteBuf, RoulettePayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString), RoulettePayload::winnerId,
            ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.STRING_UTF8), RoulettePayload::labels,
            ByteBufCodecs.VAR_INT, RoulettePayload::winnerIndex,
            ByteBufCodecs.VAR_INT, RoulettePayload::spinTicks,
            (Function4<UUID, List<String>, Integer, Integer, RoulettePayload>) RoulettePayload::new
    );

    /** NeoForge payload registration alias. */
    public static final StreamCodec<RegistryFriendlyByteBuf, RoulettePayload> STREAM_CODEC = CODEC;

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
