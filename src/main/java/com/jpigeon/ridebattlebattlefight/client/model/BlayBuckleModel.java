package com.jpigeon.ridebattlebattlefight.client.model;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.core.rider.blade.BlayBuckleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BlayBuckleModel extends GeoModel<BlayBuckleItem> {
    public BlayBuckleModel(ResourceLocation resourceLocation) {
        super();
    }

    @Override
    public ResourceLocation getModelResource(BlayBuckleItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "geo/blay_buckle.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BlayBuckleItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "textures/armor/buckle/blay_buckle.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BlayBuckleItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "animations/blay_buckle.animation.json");
    }
}
