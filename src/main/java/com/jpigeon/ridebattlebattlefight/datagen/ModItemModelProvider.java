package com.jpigeon.ridebattlebattlefight.datagen;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.item.RideBattleItems;
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
        basicItem(RideBattleItems.ORIHALCON_ELEMENT.get());
        basicItem(RideBattleItems.BLAY_BUCKLE.get());
        basicItem(RideBattleItems.BLADE_HELMET.get());
        basicItem(RideBattleItems.BLADE_CHESTPLATE.get());
        basicItem(RideBattleItems.BLADE_BOOTS.get());
    }
}