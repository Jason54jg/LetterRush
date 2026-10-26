#!/usr/bin/env python3
"""Apply common Yarn -> Mojang renames for Minecraft 26.x sources."""
from __future__ import annotations

import sys
from pathlib import Path

REPLACEMENTS = [
    ("ServerPlayerEntity", "ServerPlayer"),
    ("ServerWorld", "ServerLevel"),
    ("MinecraftClient", "Minecraft"),
    ("DrawContext", "GuiGraphics"),
    ("RegistryByteBuf", "RegistryFriendlyByteBuf"),
    ("net.minecraft.item.", "net.minecraft.world.item."),
    ("net.minecraft.entity.item.ItemEntity", "net.minecraft.world.entity.item.ItemEntity"),
    ("net.minecraft.entity.Entity", "net.minecraft.world.entity.Entity"),
    ("net.minecraft.registry.Registries", "net.minecraft.core.registries.BuiltInRegistries"),
    ("net.minecraft.util.Identifier", "net.minecraft.resources.ResourceLocation"),
    ("net.minecraft.text.Text", "net.minecraft.network.chat.Component"),
    ("net.minecraft.util.Formatting", "net.minecraft.ChatFormatting"),
    ("net.minecraft.server.command.ServerCommandSource", "net.minecraft.commands.CommandSourceStack"),
    ("net.minecraft.server.command.CommandManager", "net.minecraft.commands.Commands"),
    ("net.minecraft.particle.ParticleTypes", "net.minecraft.core.particles.ParticleTypes"),
    ("net.minecraft.sound.SoundCategory", "net.minecraft.sounds.SoundCategory"),
    ("net.minecraft.sound.SoundEvents", "net.minecraft.sounds.SoundEvents"),
    ("net.minecraft.resource.ResourceType", "net.minecraft.server.packs.resources.ResourceType"),
    ("net.minecraft.resource.SynchronousResourceReloader", "net.minecraft.server.packs.resources.SimplePreparableReloadListener"),
    ("net.minecraft.entity.boss.ServerBossBar", "net.minecraft.server.boss.ServerBossEvent"),
    ("Identifier.of(", "ResourceLocation.fromNamespaceAndPath("),
    ("Identifier.tryParse", "ResourceLocation.tryParse"),
    ("Text.literal", "Component.literal"),
    ("Text.translatable", "Component.translatable"),
    ("ItemStack.EMPTY", "ItemStack.EMPTY"),
    (".getPlayerManager()", ".getPlayerList()"),
    ("server.getOverworld()", "server.overworld()"),
    (".giveItemStack(", ".add("),
    (".sendFeedback(", ".sendSuccess("),
    (".getBlockPos()", ".blockPosition()"),
    (".getName().getString()", ".getName().getString()"),
    ("import net.minecraft.command.CommandRegistryAccess;", ""),
    ("net.minecraft.network.packet.CustomPayload", "net.minecraft.network.protocol.common.custom.CustomPacketPayload"),
    ("CustomPayload.Id", "CustomPacketPayload.Type"),
    ("CustomPayload", "CustomPacketPayload"),
    ("implements CustomPacketPayload", "implements CustomPacketPayload"),
    ("getId()", "type()"),
    ("PacketCodecs", "ByteBufCodecs"),
    ("net.minecraft.network.codec.PacketCodecs", "net.minecraft.network.codec.ByteBufCodecs"),
    ("net.minecraft.network.codec.PacketCodec", "net.minecraft.network.codec.StreamCodec"),
    ("PacketCodec<", "StreamCodec<"),
]

def main() -> int:
    root = Path(sys.argv[1] if len(sys.argv) > 1 else "common/src/main/javaMojmap26")
    for path in root.rglob("*.java"):
        text = path.read_text(encoding="utf-8")
        for old, new in REPLACEMENTS:
            text = text.replace(old, new)
        path.write_text(text, encoding="utf-8")
    return 0

if __name__ == "__main__":
    raise SystemExit(main())
