package com.jpigeon.ridebattlebattlefight.entity.client;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.entity.custom.OrihalconBeetleEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class OrihalconBeetleModel extends GeoModel<OrihalconBeetleEntity> {
    @Override
    public ResourceLocation getModelResource(OrihalconBeetleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "geo/orihalcon_beetle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(OrihalconBeetleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "textures/entity/orihalcon_beetle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(OrihalconBeetleEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "animations/orihalcon_beetle.animation.json");
    }
}
