package com.jpigeon.ridebattlebattlefight.entity.custom;

import com.jpigeon.ridebattlebattlefight.rider.BoardRiders;
import com.jpigeon.ridebattlelib.core.system.attachment.ModAttachments;
import com.jpigeon.ridebattlelib.core.system.attachment.PlayerPersistentData;
import com.jpigeon.ridebattlelib.core.system.henshin.helper.DriverActionManager;
import com.jpigeon.ridebattlelib.core.system.henshin.helper.HenshinState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// 奥利哈刚甲虫
public class OrihalconBeetleEntity extends LivingEntity implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final int MAX_LIFETIME = 200; // 10秒存在时间
    private int lifetime = 0;
    @Nullable
    private UUID ownerUUID;
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(OrihalconBeetleEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    // 弹开实体作用
    private boolean hasPushedEntities = false;


    public OrihalconBeetleEntity(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
        this.setNoGravity(true);
        this.setInvulnerable(true);
    }

    //==========GeckoLib==========
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this,
                "controller",
                0,
                this::predicate));
    }

    private PlayState predicate(AnimationState<OrihalconBeetleEntity> state) {
        state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    //==========功能==========

    @Override
    public void tick() {
        super.tick();
        lifetime++;

        if (lifetime % 5 != 0) return;
        // 超时消失
        if (lifetime >= MAX_LIFETIME) {
            this.discard();
            return;
        }

        if (!hasPushedEntities && lifetime > 0) {
            pushNearbyEntities();
            hasPushedEntities = true;
        }

        Player owner = getOwner();
        if (owner == null) {
            if (lifetime > 20) {
                discard();
            }
            return;
        }
        if (owner.isRemoved()) {
            discard();
            return;
        }
        double distance = distanceToSqr(owner);
        if (distance > 20 * 20) { // 20格外自动消失
            this.discard();
            return;
        }

        PlayerPersistentData data = owner.getData(ModAttachments.PLAYER_DATA);
        if (data.getHenshinState() != HenshinState.TRANSFORMING) {
            return;
        }

        // 使用精确碰撞检测
        if (isPlayerColliding(owner)) {
            if (!BoardRiders.BLADE_BASE_FORM.equals(data.getPendingFormId())) {
                return;
            }

            if (owner instanceof ServerPlayer serverPlayer) {
                DriverActionManager.INSTANCE.completeTransformation(serverPlayer);
                serverPlayer.level().playSound(null, serverPlayer.blockPosition(),
                        SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS,
                        1.0f, 1.0f);
            }
            discard();
        }
    }

    private boolean isPlayerColliding(Player player) {
        return player.getBoundingBox().intersects(this.getBoundingBox());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
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

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
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
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object o) {
        return 0;
    }
}
