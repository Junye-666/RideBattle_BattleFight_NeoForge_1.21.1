package com.jpigeon.ridebattlebattlefight.client.model;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.core.entity.orihalcon.OrihalconBeetle;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class OrihalconBeetleModel extends GeoModel<OrihalconBeetle> {
    @Override
    public ResourceLocation getModelResource(OrihalconBeetle animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "geo/orihalcon_beetle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OrihalconBeetle animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "textures/entity/orihalcon_beetle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OrihalconBeetle animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "animations/orihalcon_element.animation.json");
    }
}
