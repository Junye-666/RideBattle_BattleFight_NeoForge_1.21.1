package com.jpigeon.ridebattlebattlefight.core.system;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.client.playerAnimator.PlayerAnimationTrigger;
import com.jpigeon.ridebattlebattlefight.core.rider.blade.BlayBuckleItem;
import com.jpigeon.ridebattlebattlefight.core.system.network.BoardPacketHandler;
import com.jpigeon.ridebattlebattlefight.core.system.network.packet.BladeHenshinPacket;
import com.jpigeon.ridebattlebattlefight.core.rider.blade.BladeConfig;
import com.jpigeon.ridebattlelib.core.system.event.HenshinEvent;

import com.jpigeon.ridebattlelib.core.system.event.ItemInsertionEvent;
import com.jpigeon.ridebattlelib.core.system.event.SlotExtractionEvent;
import com.jpigeon.ridebattlelib.core.system.event.UnhenshinEvent;
import com.jpigeon.ridebattlelib.core.system.henshin.HenshinSystem;
import dev.kosmx.playerAnim.core.util.Ease;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;

public class BoardHenshinHandler {
    @SubscribeEvent
    public void onInsertBlade(ItemInsertionEvent.Post event) {
        if (HenshinSystem.INSTANCE.isTransformed(event.getPlayer())) return;
        if (!event.getConfig().getRiderId().equals(BladeConfig.KAMEN_RIDER_BLADE)) {
            RideBattleBattleFight.LOGGER.debug("不是剑的物品插入");
            return;
        }

        Player player = event.getPlayer();
        ItemStack buckleStack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (buckleStack.getItem() instanceof BlayBuckleItem buckleItem) {
            // 重置带扣状态
            if (player.level().isClientSide()) {
                buckleItem.resetBuckle();
            }
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && minecraft.player.getUUID().equals(player.getUUID())) {
            if (minecraft.player.level().isClientSide) {
                PlayerAnimationTrigger.playAnimation(
                        minecraft.player,
                        "blade_prepare",
                        0,
                        Ease.LINEAR
                );
            }
        }
    }

    @SubscribeEvent
    public void onHenshin(HenshinEvent.Pre event) {
        if (HenshinSystem.INSTANCE.isTransformed(event.getPlayer())) return;
        // 只处理剑的变身事件
        if (!event.getRiderId().equals(BladeConfig.KAMEN_RIDER_BLADE)) {
            RideBattleBattleFight.LOGGER.debug("不是剑的变身");
            return;
        }

        Player player = event.getPlayer();
        ItemStack buckleStack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (buckleStack.getItem() instanceof BlayBuckleItem buckleItem) {
            // 客户端触发动画
            buckleItem.playTurnUpAnimation();

        }

        if (event.getPlayer() instanceof AbstractClientPlayer clientPlayer) {
            PlayerAnimationTrigger.playAnimation(clientPlayer, "blade_action", 0, Ease.LINEAR);
        }

        // 客户端发送数据包给服务器
        if (event.getPlayer().level().isClientSide()) {
            RideBattleBattleFight.LOGGER.debug("发送实体生成数据包");
            BoardPacketHandler.sendToServer(new BladeHenshinPacket(event.getPlayer().getUUID()
            ));
        }

    }

    @SubscribeEvent
    public void onUnHenshin(UnhenshinEvent.Post event) {
        Player player = event.getPlayer();
        ItemStack buckleStack = player.getItemBySlot(EquipmentSlot.LEGS);

        if (buckleStack.getItem() instanceof BlayBuckleItem buckleItem) {
            // 客户端重置带扣状态
            buckleItem.resetBuckle();
        }
    }

    @SubscribeEvent
    public void onItemExtract(SlotExtractionEvent.Post event) {
        if (event.getConfig().getRiderId() != BladeConfig.KAMEN_RIDER_BLADE) return;
        if (HenshinSystem.INSTANCE.isTransformed(event.getPlayer())) return;
        ItemStack buckleStack = event.getPlayer().getItemBySlot(EquipmentSlot.LEGS);

        if (buckleStack.getItem() instanceof BlayBuckleItem buckleItem)
            buckleItem.resetBuckle();
    }
}
