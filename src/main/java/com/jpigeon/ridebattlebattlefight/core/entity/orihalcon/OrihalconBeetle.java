package com.jpigeon.ridebattlebattlefight.core.entity.orihalcon;

import com.jpigeon.ridebattlebattlefight.core.rider.blade.BladeConfig;
import com.jpigeon.ridebattlelib.core.system.attachment.RiderAttachments;
import com.jpigeon.ridebattlelib.core.system.attachment.RiderData;
import com.jpigeon.ridebattlelib.core.system.henshin.helper.DriverActionManager;
import com.jpigeon.ridebattlelib.core.system.henshin.helper.HenshinState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;
import software.bernie.geckolib.util.RenderUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// 奥利哈刚甲虫
public class OrihalconBeetle extends OrihalconEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final int MAX_LIFETIME = 200; // 10秒存在时间
    private int lifetime = 0;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(OrihalconBeetle.class, EntityDataSerializers.OPTIONAL_UUID);
    // 弹开实体作用
    private boolean hasPushedEntities = false;
    public int disappearTimer = 0;
    public static final int DISAPPEAR_DURATION = 20; // 1秒消失时间
    private boolean hasTriggeredTransformation = false; // 确保只触发一次变身

    public OrihalconBeetle(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    //==========GeckoLib==========
    public static final RawAnimation ORIHALCON_APPEAR = RawAnimation.begin()
            .then("appear", Animation.LoopType.PLAY_ONCE); // 明确指定播放一次

    public static final RawAnimation ORIHALCON_IDLE = RawAnimation.begin()
            .thenLoop("idle"); // 循环播放空闲动画

    public static final RawAnimation ORIHALCON_DISAPPEAR = RawAnimation.begin()
            .thenPlayAndHold("passThrough"); // 使用playAndHold

    private AnimationController<OrihalconBeetle> disappearController;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {



        controllers.add(new AnimationController<>(this, "appear_controller", 0, this::handleAppearAnimation));
        controllers.add(new AnimationController<>(this, "idle_controller", 0, this::handleIdleAnimation));
        disappearController = new AnimationController<>(this, "disappear_controller", 0, this::handleDisappearAnimation);
        controllers.add(disappearController);
    }

    private enum AnimState {APPEARING, IDLING, DISAPPEARING}

    private AnimState animState = AnimState.APPEARING;

    // ========== 分离的动画处理函数 ==========

    private PlayState handleAppearAnimation(AnimationState<OrihalconBeetle> state) {
        // 只处理出现动画
        if (animState != AnimState.APPEARING) {
            return PlayState.STOP;
        }

        if (!state.isCurrentAnimation(ORIHALCON_APPEAR)) {
            state.getController().setAnimation(ORIHALCON_APPEAR);
        }

        // 动画结束后自动切换到空闲状态
        if (state.getController().getAnimationState() == AnimationController.State.STOPPED) {
            animState = AnimState.IDLING;
        }

        return PlayState.CONTINUE;
    }

    private PlayState handleIdleAnimation(AnimationState<OrihalconBeetle> state) {
        // 只处理空闲动画
        if (animState != AnimState.IDLING) {
            return PlayState.STOP;
        }

        if (!state.isCurrentAnimation(ORIHALCON_IDLE)) {
            state.getController().setAnimation(ORIHALCON_IDLE);
        }

        return PlayState.CONTINUE;
    }

    private PlayState handleDisappearAnimation(AnimationState<OrihalconBeetle> state) {
        // 只处理消失动画
        if (animState != AnimState.DISAPPEARING) {
            return PlayState.STOP;
        }

        if (!state.isCurrentAnimation(ORIHALCON_DISAPPEAR)) {
            state.getController().setAnimation(ORIHALCON_DISAPPEAR);
        }

        return PlayState.CONTINUE;
    }

    // ========== 外部调用方法 ==========

    public void triggerDisappearAnimation() {
        // 确保控制器已初始化
        if (disappearController == null) {
            AnimatableInstanceCache cache = getAnimatableInstanceCache();
            if (cache != null) {
                AnimatableManager<GeoAnimatable> manager = cache.getManagerForId(getId());
                if (manager != null) {
                    disappearController = new AnimationController<>(
                            this, "disappear_controller", 0, this::handleDisappearAnimation
                    );
                    manager.addController(disappearController);
                }
            }

            if (disappearController == null) {
                return;
            }
        }

        this.animState = AnimState.DISAPPEARING;
        this.disappearController.forceAnimationReset();
        this.disappearTimer = DISAPPEAR_DURATION;
    }

    //==========功能==========

    @Override
    public void tick() {
        super.tick();
        lifetime++;

        if (animState == AnimState.DISAPPEARING) {
            disappearTimer--;
            if (disappearTimer <= 0) {
                this.discard();
                return;
            }
        }

        if (animState == AnimState.DISAPPEARING) {
            return;
        }

        if (lifetime % 5 != 0) return;

        // 超时消失改为触发消失动画
        if (lifetime >= MAX_LIFETIME) {
            startDisappearing();
            return;
        }

        if (!hasPushedEntities && lifetime > 0) {
            pushNearbyEntities();
            hasPushedEntities = true;
        }

        Player owner = getOwner();
        if (owner == null) {
            if (lifetime > 20) {
                startDisappearing();
            }
            return;
        }

        if (owner.isRemoved()) {
            startDisappearing();
            return;
        }

        double distance = distanceToSqr(owner);
        if (distance > 20 * 20) {
            startDisappearing();
            return;
        }

        RiderData data = owner.getData(RiderAttachments.RIDER_DATA);
        if (data.getHenshinState() != HenshinState.TRANSFORMING) {
            return;
        }

        // 使用精确碰撞检测
        if (isPlayerColliding(owner)) {
            if (!BladeConfig.BLADE_BASE_FORM.equals(data.getPendingFormId())) {
                return;
            }

            // 确保只触发一次变身
            if (!hasTriggeredTransformation && owner instanceof ServerPlayer serverPlayer) {
                DriverActionManager.INSTANCE.completeTransformation(serverPlayer);
                serverPlayer.level().playSound(null, serverPlayer.blockPosition(),
                        SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS,
                        1.0f, 1.0f);
                hasTriggeredTransformation = true;
            }

            // 触发消失动画
            startDisappearing();
        }
    }

    public void startDisappearing() {
        if (animState != AnimState.DISAPPEARING) {
            triggerDisappearAnimation();
            disappearTimer = DISAPPEAR_DURATION;
            hasTriggeredTransformation = true;

            // 立即停止所有运动
            this.setDeltaMovement(Vec3.ZERO);

            // 播放消失音效
            if (!level().isClientSide) {
                level().playSound(null, blockPosition(),
                        SoundEvents.ENDERMAN_TELEPORT,
                        SoundSource.PLAYERS, 0.5f, 1.5f);
            }
        }
    }

    public float getCurrentAlpha() {
        if (animState == AnimState.DISAPPEARING) {
            // 使用缓动函数使透明度变化更平滑
            float progress = (float) disappearTimer / DISAPPEAR_DURATION;
            // 二次方缓出效果，最后阶段消失更快
            return progress * progress;
        }
        return 0.8f;
    }

    @Override
    public boolean isInvisible() {
        // 当透明度低于0.1时视为不可见
        return getCurrentAlpha() < 0.1f || super.isInvisible();
    }

    private boolean isPlayerColliding(Player player) {
        return player.getBoundingBox().intersects(this.getBoundingBox());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 100.0)
                .add(Attributes.FOLLOW_RANGE, 0.0);
    }

    private void pushNearbyEntities() {
        Player owner = getOwner();
        if (owner == null) return;

        // 获取4格半径内的生物（排除自己和owner）
        List<LivingEntity> entities = level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(4.0),
                e -> e != this && e != owner
        );

        for (LivingEntity entity : entities) {
            // 计算弹开方向（从实体指向远离玩家的方向）
            Vec3 awayDirection = entity.position()
                    .subtract(owner.position())
                    .normalize()
                    .scale(1.5); // 弹开力度

            // 应用击退效果
            entity.setDeltaMovement(
                    awayDirection.x,
                    Math.min(0.5, awayDirection.y + 0.25), // 轻微上抛
                    awayDirection.z
            );
            entity.hurtMarked = true; // 强制同步移动
        }

        // 播放音效
        level().playSound(null, blockPosition(),
                SoundEvents.PLAYER_ATTACK_KNOCKBACK,
                SoundSource.PLAYERS, 1.0F, 1.2F);
    }

    //==========Setter方法==========

    public void setOwner(@Nullable Player player) {
        if (player != null) {
            this.entityData.set(OWNER_UUID, Optional.of(player.getUUID()));
        } else {
            this.entityData.set(OWNER_UUID, Optional.empty());
        }
    }

    public void setFacingPlayer(Player player) {
        // 计算玩家到实体的向量
        Vec3 toEntity = this.position().subtract(player.position());

        // 计算Y旋转角度（实体背对玩家）
        float yRot = (float) Math.toDegrees(Math.atan2(toEntity.z, toEntity.x)) - 90.0F;

        // 设置实体旋转
        this.setYRot(yRot);
        this.yRotO = yRot;

        // 立即更新碰撞箱
        this.refreshDimensions();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER_UUID, Optional.empty());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        entityData.get(OWNER_UUID).ifPresent(uuid -> tag.putUUID("OwnerUUID", uuid));
    }

    //==========Getter方法==========

    @Nullable
    public Player getOwner() {
        Optional<UUID> uuid = this.entityData.get(OWNER_UUID);
        return uuid.map(value -> level().getPlayerByUUID(value)).orElse(null);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        if (tag.hasUUID("OwnerUUID")) {
            this.entityData.set(OWNER_UUID, Optional.of(tag.getUUID("OwnerUUID")));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object o) {
        return RenderUtil.getCurrentTick();
    }

    @Override
    public void push(@NotNull Entity entity) {
        // 完全禁用实体间的推动
    }

    @Override
    public void push(double x, double y, double z) {
        // 禁用所有外力推动
    }

    @Override
    public boolean isCustomNameVisible() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    // 被迫实现
    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return false;
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot equipmentSlot, @NotNull ItemStack itemStack) {
    }
}
