package com.jpigeon.ridebattlebattlefight.core.item;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;

import com.jpigeon.ridebattlebattlefight.core.rider.blade.BladeArmorItem;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BattleFightItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RideBattleBattleFight.MODID);

    // 材料
    public static final DeferredItem<Item> ORIHALCON_ELEMENT = ITEMS.register("orihalcon_element",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));

    //卡片


    // 盔甲

    public static final DeferredItem<BladeArmorItem> BLADE_HELMET = ITEMS.register("blade_helmet",
            () -> new BladeArmorItem(BoardArmorMaterials.BOARD_SYSTEM_MATERIAL, ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BladeArmorItem> BLADE_CHESTPLATE = ITEMS.register("blade_chestplate",
            () -> new BladeArmorItem(BoardArmorMaterials.BOARD_SYSTEM_MATERIAL, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BladeArmorItem> BLAY_BUCKLE  = ITEMS.register("blay_buckle",
            () -> new BladeArmorItem(BoardArmorMaterials.BOARD_SYSTEM_MATERIAL, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BladeArmorItem> BLADE_BOOTS  = ITEMS.register("blade_boots",
            () -> new BladeArmorItem(BoardArmorMaterials.BOARD_SYSTEM_MATERIAL, ArmorItem.Type.BOOTS,
                    new Item.Properties().stacksTo(1)));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
