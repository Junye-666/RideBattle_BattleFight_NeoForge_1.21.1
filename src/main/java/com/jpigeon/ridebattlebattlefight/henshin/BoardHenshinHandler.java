package com.jpigeon.ridebattlebattlefight.henshin;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.network.BoardPacketHandler;
import com.jpigeon.ridebattlebattlefight.network.packet.BladeHenshinPacket;
import com.jpigeon.ridebattlebattlefight.rider.BoardRiders;
import com.jpigeon.ridebattlelib.core.system.event.HenshinEvent;

import com.jpigeon.ridebattlelib.core.system.henshin.HenshinSystem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;

public class BoardHenshinHandler {
    @SubscribeEvent
    public void onHenshin(HenshinEvent.Pre event) {
        if (HenshinSystem.INSTANCE.isTransformed(event.getPlayer())) return;
        // 只处理剑的变身事件
        if (!event.getRiderId().equals(BoardRiders.KAMEN_RIDER_BLADE)) {
            RideBattleBattleFight.LOGGER.debug("不是剑的变身");
            return;
        }

        RideBattleBattleFight.LOGGER.debug("pendingFormId:{}", event.getFormId());

        // 客户端发送数据包给服务器
        if (event.getSide() == LogicalSide.CLIENT) {
            RideBattleBattleFight.LOGGER.debug("发送实体生成数据包");
            BoardPacketHandler.sendToServer(new BladeHenshinPacket(event.getPlayer().getUUID()
            ));
        }
    }
}
