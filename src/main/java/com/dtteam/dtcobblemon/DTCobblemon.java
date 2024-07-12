package com.dtteam.dtcobblemon;

import com.dtteam.dtcobblemon.fruit.ApricornFruit;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DTCobblemon.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DTCobblemon {
    public static final String MODID = "dtcobblemon";
    public static final Logger LOGGER = LogManager.getLogger();

    public DTCobblemon() {
        RegistryHandler.setup(MODID);
    }


    @SubscribeEvent
    public static void registryEvent(TypeRegistryEvent<Fruit> event) {
        event.registerType(new ResourceLocation(DTCobblemon.MODID, "apricorn"), ApricornFruit.TYPE);
    }

}
