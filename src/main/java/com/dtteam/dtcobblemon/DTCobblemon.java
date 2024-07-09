package com.dtteam.dtcobblemon;

import com.cobblemon.mod.common.config.CobblemonConfig;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DTCobblemon.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DTCobblemon {
    public static final String MODID = "dtcobblemon";
    public static final Logger LOGGER = LogManager.getLogger();

    public DTCobblemon() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        RegistryHandler.setup(MODID);
    }

}
