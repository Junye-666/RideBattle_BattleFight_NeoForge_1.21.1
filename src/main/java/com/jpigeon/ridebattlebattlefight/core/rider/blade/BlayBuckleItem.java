package com.jpigeon.ridebattlebattlefight.core.rider.blade;

import com.jpigeon.ridebattlebattlefight.client.renderer.BlayBuckleRenderer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public class BlayBuckleItem extends BladeArmorItem implements GeoItem {
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    private AnimationController<BlayBuckleItem> turnUpController;
    private AnimationController<BlayBuckleItem> idleCloseController;
    private AnimationController<BlayBuckleItem> idleOpenController;

    public BlayBuckleItem(Holder<ArmorMaterial> material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoArmorRenderer<?> renderer;

            @Override
            public <T extends LivingEntity> HumanoidModel<?> getGeoArmorRenderer(
                    @Nullable T livingEntity, ItemStack itemStack,
                    @Nullable EquipmentSlot equipmentSlot, @Nullable HumanoidModel<T> original) {
                if (this.renderer == null)
                    this.renderer = new BlayBuckleRenderer();
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        // 初始化控制器并保存引用
        turnUpController = new AnimationController<>(this, "turnUp_controller", 0, this::handleTurnUpAnimation);
        idleCloseController = new AnimationController<>(this, "idleClose_controller", 0, this::handleIdleCloseAnimation);
        idleOpenController = new AnimationController<>(this, "idleOpen_controller", 0, this::handleIdleOpenAnimation);

        controllerRegistrar.add(idleCloseController);
        controllerRegistrar.add(idleOpenController);
        controllerRegistrar.add(turnUpController);
    }

    public enum AnimState { CLOSED, TURN_UP, OPEN }
    private AnimState animState = AnimState.CLOSED;

    // 重置带扣状态（关键修复）
    public void resetBuckle() {
        this.animState = AnimState.CLOSED;
        if (idleCloseController != null) {
            idleCloseController.forceAnimationReset();
        }
    }

    // 开始变身动画
    public void playTurnUpAnimation() {
        // 确保状态重置为CLOSED后可以再次激活
        if (animState == AnimState.OPEN) {
            resetBuckle();
        }

        this.animState = AnimState.TURN_UP;
        if (turnUpController != null) {
            turnUpController.forceAnimationReset();
        }
    }

    private PlayState handleIdleCloseAnimation(AnimationState<BlayBuckleItem> animationState) {
        // 添加状态检查（关键修复）
        if (animState != AnimState.CLOSED) return PlayState.STOP;

        animationState.getController().setAnimation(RawAnimation.begin().then("idleClose", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private PlayState handleIdleOpenAnimation(AnimationState<BlayBuckleItem> animationState) {
        // 添加状态检查（关键修复）
        if (animState != AnimState.OPEN) return PlayState.STOP;

        animationState.getController().setAnimation(RawAnimation.begin().then("idleOpen", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    private PlayState handleTurnUpAnimation(AnimationState<BlayBuckleItem> animationState) {
        if (animState != AnimState.TURN_UP) {
            return PlayState.STOP;
        }

        RawAnimation turnUp = RawAnimation.begin().then("turnUp", Animation.LoopType.PLAY_ONCE);
        if (!animationState.isCurrentAnimation(turnUp)) {
            animationState.getController().setAnimation(turnUp);
        }

        // 动画结束后自动切换到打开状态
        if (animationState.getController().getAnimationState() == AnimationController.State.STOPPED) {
            this.animState = AnimState.OPEN;
            if (idleOpenController != null) {
                idleOpenController.forceAnimationReset();
            }
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}