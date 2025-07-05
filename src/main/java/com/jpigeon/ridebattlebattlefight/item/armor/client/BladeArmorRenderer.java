package com.jpigeon.ridebattlebattlefight.item.armor.client;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.item.armor.blade.BladeArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BladeArmorRenderer extends GeoArmorRenderer<BladeArmorItem> {
    public BladeArmorRenderer() {
        super(new BladeArmorModel(ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "geo/blade_armor")));
    }
}