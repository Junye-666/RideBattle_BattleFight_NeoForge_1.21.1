package com.jpigeon.ridebattlebattlefight.network.packet;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlelib.core.system.network.handler.UUIDStreamCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record BladeHenshinPacket(UUID playerId) implements CustomPacketPayload {
    public static final ResourceLocation ID =
            ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "spawn_orihalcon");

    public static final Type<BladeHenshinPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, BladeHenshinPacket> STREAM_CODEC =
            StreamCodec.composite(
                    UUIDStreamCodec.INSTANCE,
                    BladeHenshinPacket::playerId,
                    BladeHenshinPacket::new
            );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() { return TYPE; }
}
