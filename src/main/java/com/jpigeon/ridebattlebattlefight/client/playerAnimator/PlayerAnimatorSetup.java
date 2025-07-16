package com.jpigeon.ridebattlebattlefight.client.playerAnimator;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationFactory;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = RideBattleBattleFight.MODID, bus = EventBusSubscriber.Bus.MOD)
public class PlayerAnimatorSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PlayerAnimationFactory.ANIMATION_DATA_FACTORY.registerFactory(
                ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "animation"),
                42,
                PlayerAnimatorSetup::registerPlayerAnimation
        );
    }

    private static IAnimation registerPlayerAnimation(AbstractClientPlayer player) {
        return new ModifierLayer<>();
    }
}