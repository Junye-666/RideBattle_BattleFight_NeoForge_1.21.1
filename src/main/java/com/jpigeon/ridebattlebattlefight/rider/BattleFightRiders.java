package com.jpigeon.ridebattlebattlefight.rider;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import net.minecraft.resources.ResourceLocation;

public class BattleFightRiders {
    public static final ResourceLocation KAMEN_RIDER_BLADE  =
            ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "kamen_rider_blade");
    public static final ResourceLocation BLAY_BUCKLE_CARD_SLOT =
            ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "blay_buckle_card_slot");
    public static void setKamenRiderBlade() {
        RiderConfig KamenRiderBlade =  new RiderConfig(KAMEN_RIDER_BLADE)


                ;
    }
}
