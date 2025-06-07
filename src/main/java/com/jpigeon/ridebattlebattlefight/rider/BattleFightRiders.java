package com.jpigeon.ridebattlebattlefight.rider;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.item.BattleFightItems;
import com.jpigeon.ridebattlelib.core.system.form.FormConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderConfig;
import com.jpigeon.ridebattlelib.core.system.henshin.RiderRegistry;
import com.jpigeon.ridebattlelib.core.system.henshin.TriggerType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;

import java.util.List;

public class BattleFightRiders {
    // 骑士Id
    public static final ResourceLocation KAMEN_RIDER_BLADE =
            ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "kamen_rider_blade");
    // 槽位
    public static final ResourceLocation BLAY_BUCKLE_CARD_SLOT =
            ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "blay_buckle_card_slot");
    // 形态Id
    public static final ResourceLocation BLADE_BASE_FORM =
            ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "blade_base_form");

    // 实际注册
    public static void registerBlade() {
        RiderConfig KamenRiderBlade = new RiderConfig(KAMEN_RIDER_BLADE)
                .setDriverItem(BattleFightItems.BLAY_BUCKLE.get(), EquipmentSlot.LEGS)
                .addSlot(BLAY_BUCKLE_CARD_SLOT,
                        List.of(Items.IRON_INGOT),
                        true,
                        false)
                .setTriggerType(TriggerType.KEY);
        FormConfig bladeBaseForm = new FormConfig(BLADE_BASE_FORM)
                .setArmor(BattleFightItems.BLADE_HELMET.get(),
                        BattleFightItems.BLADE_CHESTPLATE.get(),
                        null,
                        BattleFightItems.BLADE_BOOTS.get())
                .addAttribute(
                        ResourceLocation.fromNamespaceAndPath("minecraft", "generic.movement_speed"),
                        0.1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                )
                .addEffect(
                        MobEffects.NIGHT_VISION,
                        0,
                        114514,
                        true
                )
                .addRequiredItem(BLAY_BUCKLE_CARD_SLOT, Items.IRON_INGOT)
                .addGrantedItem(Items.IRON_SWORD.getDefaultInstance());
        KamenRiderBlade
                .addForm(bladeBaseForm)
                .setBaseForm(bladeBaseForm.getFormId());
        bladeBaseForm.setAllowsEmptyBelt(false);

        RiderRegistry.registerRider(KamenRiderBlade);
    }

    public static void init (){
        registerBlade();
    }
}
