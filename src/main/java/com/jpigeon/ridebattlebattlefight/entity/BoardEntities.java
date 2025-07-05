package com.jpigeon.ridebattlebattlefight.entity;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import com.jpigeon.ridebattlebattlefight.entity.custom.OrihalconBeetleEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class BoardEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RideBattleBattleFight.MODID);

    public static final Supplier<EntityType<OrihalconBeetleEntity>> ORIHALCON_BEETLE =
            ENTITY_TYPES.register("orihalcon_beetle",
                    () -> EntityType.Builder.of(OrihalconBeetleEntity::new, MobCategory.MISC)
                            .sized(0.5f, 2.0f) // 碰撞箱尺寸
                            .clientTrackingRange(32)
                            .build("orihalcon_beetle")
            );

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ORIHALCON_BEETLE.get(), OrihalconBeetleEntity.createAttributes().build());
    }

}
