package com.jpigeon.ridebattlebattlefight.item.client;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.item.armor.blade.BladeArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BladeArmorModel extends GeoModel<BladeArmorItem> {
    public BladeArmorModel(ResourceLocation resourceLocation) {
        super();
    }

    @Override
    public ResourceLocation getModelResource(BladeArmorItem animatable) {
        ResourceLocation modelResource = ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "geo/blade_armor.geo.json");
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(BladeArmorItem animatable) {
        ResourceLocation textureResource = ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "textures/armor/blade_armor.png");
        return textureResource;
    }

    @Override
    public ResourceLocation getAnimationResource(BladeArmorItem animatable) {
        ResourceLocation animationResource = ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "animations/blade_armor.animation.json");
        return animationResource;
    }
}
