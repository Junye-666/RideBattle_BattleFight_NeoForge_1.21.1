package com.jpigeon.ridebattlebattlefight.core.item.card;


import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CardItem extends Item {
    public CardItem() {
        super(new Properties().stacksTo(64));
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) {
        return false; // 可改为 true 以启用闪光效果
    }
}
