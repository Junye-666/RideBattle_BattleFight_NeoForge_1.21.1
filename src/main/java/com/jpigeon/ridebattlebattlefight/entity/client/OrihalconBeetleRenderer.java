package com.jpigeon.ridebattlebattlefight.entity.client;

import com.jpigeon.ridebattlebattlefight.entity.custom.OrihalconBeetleEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OrihalconBeetleRenderer extends GeoEntityRenderer<OrihalconBeetleEntity> {

    public OrihalconBeetleRenderer(EntityRendererProvider.Context renderManager, GeoModel model) {
        super(renderManager, model);
        this.shadowRadius = 0.0f;
    }

    @Override
    public OrihalconBeetleEntity getAnimatable() {
        return super.getAnimatable();
    }

    @Override
    public void render(OrihalconBeetleEntity entity, float entityYaw, float partialTick, PoseStack poseStack,
                       MultiBufferSource buffer, int packedLight) {
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 0.7f);

        super.render(entity, entityYaw, partialTick, poseStack, buffer, packedLight);
        RenderSystem.disableBlend();
    }
}
