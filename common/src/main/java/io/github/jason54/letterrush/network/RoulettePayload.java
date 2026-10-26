package io.github.jason54.letterrush.network;

import io.github.jason54.letterrush.LetterRush;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Server → client payload that drives the optional roulette overlay.
 */
public record RoulettePayload(UUID winnerId, List<String> labels, int winnerIndex, int spinTicks) implements CustomPayload {
    public static final CustomPayload.Id<RoulettePayload> ID =
            new CustomPayload.Id<>(Identifier.of(LetterRush.MOD_ID, "roulette"));

    public static final PacketCodec<RegistryByteBuf, RoulettePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.STRING.xmap(UUID::fromString, UUID::toString), RoulettePayload::winnerId,
            PacketCodecs.collection(ArrayList::new, PacketCodecs.STRING), RoulettePayload::labels,
            PacketCodecs.VAR_INT, RoulettePayload::winnerIndex,
            PacketCodecs.VAR_INT, RoulettePayload::spinTicks,
            RoulettePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
