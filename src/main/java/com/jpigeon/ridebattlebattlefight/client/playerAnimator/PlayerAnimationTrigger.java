package com.jpigeon.ridebattlebattlefight.client.playerAnimator;

import com.jpigeon.ridebattlebattlefight.RideBattleBattleFight;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AbstractFadeModifier;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationRegistry;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public class PlayerAnimationTrigger {
    /**
     * 在任何需要的地方触发动画
     * @param player 目标玩家实体
     * @param animationId 动画资源ID (如 "waving")
     * @param fadeDuration 淡入淡出时间（tick）
     * @param easeType 缓动函数类型（如 Ease.LINEAR, Ease.QUAD_IN_OUT）
     */
    public static void playAnimation(
            AbstractClientPlayer player,
            String animationId,
            int fadeDuration,
            Ease easeType) {

// 1. 获取动画层
        ModifierLayer<IAnimation> animationLayer = (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player)
                .get(ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, "animation"));

        if (animationLayer == null) {
            return; // 动画层不可用，直接返回
        }

// 2. 获取动画资源
        IAnimation animationResource = Objects.requireNonNull(PlayerAnimationRegistry.getAnimation(
                ResourceLocation.fromNamespaceAndPath(RideBattleBattleFight.MODID, animationId)
        )).playAnimation();

// 3. 创建淡入修饰器（使用传入的缓动函数）
        AbstractFadeModifier fadeModifier = AbstractFadeModifier.standardFadeIn(fadeDuration, easeType);

// 4. 替换动画并应用淡入效果
        animationLayer.replaceAnimationWithFade(fadeModifier, animationResource);
    }

    public static void playAnimation(AbstractClientPlayer player, String animationId, int fadeDuration) {
        playAnimation(player, animationId, fadeDuration, Ease.LINEAR);
    }
}
