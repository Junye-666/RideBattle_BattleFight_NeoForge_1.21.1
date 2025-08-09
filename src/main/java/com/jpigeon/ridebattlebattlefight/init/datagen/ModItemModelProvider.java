package com.jpigeon.ridebattlebattlefight.init.datagen;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.core.item.BattleFightItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;


public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RideBattleBattleFight.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        LOGGER.error("[DEBUG] Registering item models...");
        basicItem(BattleFightItems.ORIHALCON_ELEMENT.get());
        basicItem(BattleFightItems.BLAY_BUCKLE.get());
        basicItem(BattleFightItems.BLADE_HELMET.get());
        basicItem(BattleFightItems.BLADE_CHESTPLATE.get());
        basicItem(BattleFightItems.BLADE_BOOTS.get());
        basicItem(BattleFightItems.SPADE_ACE.get());
    }
}