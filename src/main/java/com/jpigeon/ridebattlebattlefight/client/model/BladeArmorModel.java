package com.jpigeon.ridebattlebattlefight.client.model;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.core.rider.blade.BladeArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BladeArmorModel extends GeoModel<BladeArmorItem> {
    public BladeArmorModel(ResourceLocation resourceLocation) {
        super();
    }

    @Override
    public ResourceLocation getModelResource(BladeArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "geo/blade_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BladeArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "textures/armor/blade_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BladeArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "animations/blade_armor.animation.json");
    }
}
