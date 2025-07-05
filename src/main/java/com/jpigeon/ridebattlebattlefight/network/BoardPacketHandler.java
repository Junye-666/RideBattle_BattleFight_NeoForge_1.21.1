package com.jpigeon.ridebattlebattlefight.network;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.entity.BoardEntities;
import com.jpigeon.ridebattlebattlefight.entity.custom.OrihalconBeetleEntity;
import com.jpigeon.ridebattlebattlefight.network.packet.BladeHenshinPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class BoardPacketHandler {
    public static void register(final RegisterPayloadHandlersEvent event) {
        event.registrar(RideBattleBattleFight.MODID)
                .playToServer(
                        BladeHenshinPacket.TYPE, BladeHenshinPacket.STREAM_CODEC,
                        (packet, context) -> {
                            Player player = context.player();
                            if (player instanceof ServerPlayer serverPlayer) {
                                spawnOrihalconBeetle(serverPlayer);
                            }
                        });
    }

    public static void sendToServer(CustomPacketPayload packet) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(packet);
        }
    }

    public static void sendToClient(ServerPlayer player, CustomPacketPayload packet) {
        player.connection.send(packet);
    }

    private static final Map<UUID, Long> lastSummonTime = new ConcurrentHashMap<>();
    private static final long SUMMON_COOLDOWN = 5000; // 5秒冷却

    private static void spawnOrihalconBeetle(ServerPlayer player) {
        // 检查冷却时间
        long currentTime = System.currentTimeMillis();
        Long lastTime = lastSummonTime.get(player.getUUID());

        if (lastTime != null && (currentTime - lastTime) < SUMMON_COOLDOWN) {
            player.displayClientMessage(
                    Component.literal("Orihalcon元素尚未恢复!").withStyle(ChatFormatting.BLUE),
                    true
            );
            return;
        }

        List<OrihalconBeetleEntity> existing = player.serverLevel()
                .getEntitiesOfClass(OrihalconBeetleEntity.class,
                        player.getBoundingBox().inflate(20),
                        e -> player.equals(e.getOwner())
                );

        if (!existing.isEmpty()) {
            player.displayClientMessage(
                    Component.literal("已释放Orihalcon元素！").withStyle(ChatFormatting.AQUA),
                    true
            );
            return;
        }

        // 计算位置
        Vec3 look = player.getLookAngle();
        Vec3 spawnPos = player.position()
                .add(look.x * 3.0, 0, look.z * 3.0);


        ServerLevel level = player.serverLevel();
        OrihalconBeetleEntity entity = new OrihalconBeetleEntity(
                BoardEntities.ORIHALCON_BEETLE.get(),
                level
        );

        entity.setOwner(player); // 设置所有者
        entity.setPos(spawnPos);
        entity.setFacingPlayer(player); // 关键：正确设置朝向
        entity.lookAt(EntityAnchorArgument.Anchor.EYES, player.position().add(0, player.getEyeHeight(), 0));



        if (level.addFreshEntity(entity)) {
            lastSummonTime.put(player.getUUID(), currentTime);
            RideBattleBattleFight.LOGGER.info("服务器成功生成Orihalcon实体，所有者: {}", player.getName().getString());

            // 播放音效
            level.playSound(
                    null,
                    player.blockPosition(),
                    SoundEvents.AMETHYST_BLOCK_CHIME,
                    SoundSource.PLAYERS,
                    1.0f,
                    1.0f
            );
        } else {
            RideBattleBattleFight.LOGGER.error("实体生成失败");
        }
    }
}
