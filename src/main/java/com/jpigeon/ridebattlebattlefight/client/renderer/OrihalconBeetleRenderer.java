package com.jpigeon.ridebattlebattlefight.client.renderer;

import com.jpigeon.ridebattlebattlefight.core.entity.orihalcon.OrihalconBeetle;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class OrihalconBeetleRenderer extends GeoEntityRenderer<OrihalconBeetle> {

    public OrihalconBeetleRenderer(EntityRendererProvider.Context renderManager, GeoModel model) {
        super(renderManager, model);
        this.shadowRadius = 0.0f;
    }

    @Override
    public void render(OrihalconBeetle entity, float entityYaw, float partialTicks,
                       @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {

        // 获取当前透明度
        float alpha = entity.getCurrentAlpha();

        // 完全透明时不渲染
        if (alpha < 0.01f) {
            return;
        }

        // 应用透明度前保存当前状态
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // 应用透明度
        poseStack.pushPose();

        // 设置透明度
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, alpha);

        // 调用父类渲染
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);

        // 恢复渲染状态
        poseStack.popPose();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.disableBlend();
    }

    @Override
    public @Nullable RenderType getRenderType(OrihalconBeetle animatable, ResourceLocation texture,
                                              @Nullable MultiBufferSource bufferSource, float partialTick) {
        // 使用半透明渲染类型
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public OrihalconBeetle getAnimatable() {
        return super.getAnimatable();
    }
}
