package com.jpigeon.ridebattlebattlefight.client.renderer;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.client.model.BlayBuckleModel;
import com.jpigeon.ridebattlebattlefight.core.rider.blade.BlayBuckleItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class BlayBuckleRenderer extends GeoArmorRenderer<BlayBuckleItem> {
    public BlayBuckleRenderer() {
        super(new BlayBuckleModel(ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "geo/blay_buckle")));
    }
}