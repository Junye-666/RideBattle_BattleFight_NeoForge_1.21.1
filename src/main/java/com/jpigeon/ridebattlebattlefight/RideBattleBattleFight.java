package com.jpigeon.ridebattlebattlefight;

import com.jpigeon.ridebattlebattlefight.entity.BoardEntities;
import com.jpigeon.ridebattlebattlefight.entity.client.OrihalconBeetleModel;
import com.jpigeon.ridebattlebattlefight.entity.client.OrihalconBeetleRenderer;
import com.jpigeon.ridebattlebattlefight.henshin.BoardHenshinHandler;
import com.jpigeon.ridebattlebattlefight.item.BattleFightItems;
import com.jpigeon.ridebattlebattlefight.network.BoardPacketHandler;
import com.jpigeon.ridebattlebattlefight.rider.BoardRiders;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(RideBattleBattleFight.MODID)
public class RideBattleBattleFight {
    public static final String MODID = "ridebattlebattlefight";
    public static final Logger LOGGER = LogUtils.getLogger();

    public RideBattleBattleFight(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(BoardPacketHandler::register);

        NeoForge.EVENT_BUS.register(this);

        BattleFightItems.register(modEventBus);
        BoardEntities.register(modEventBus);

        NeoForge.EVENT_BUS.register(new BoardHenshinHandler());

        modEventBus.addListener(this::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        NeoForge.EVENT_BUS.addListener(this::onServerStarting);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            BoardRiders.init();
        }

        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            BoardEntities.registerAttributes(event);
        }

        @SubscribeEvent
        public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerEntityRenderer(
                    BoardEntities.ORIHALCON_BEETLE.get(),
                    context -> new OrihalconBeetleRenderer(context, new OrihalconBeetleModel())
            );
        }
    }
}
