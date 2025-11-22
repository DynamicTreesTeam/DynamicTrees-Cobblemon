package com.dtteam.dtcobblemon;

import com.dtteam.dtcobblemon.fruit.ApricornFruit;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.registry.NeoForgeRegistryHandler;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

@Mod(DynamicTreesCobblemon.MOD_ID)
@EventBusSubscriber(modid = DynamicTreesCobblemon.MOD_ID)
public class DynamicTreesCobblemon {
    public static final String MOD_ID = "dtcobblemon";

    public DynamicTreesCobblemon(IEventBus bus, ModContainer modContainer) {
        //NeoForgeRegistryHandler.setup(MOD_ID, bus);
    }

    @SubscribeEvent
    public static void registryEvent(TypeRegistryEvent<Fruit> event) {
        if (event.isEntryOfType(Fruit.class)){
            event.registerType(ResourceLocation.fromNamespaceAndPath(DynamicTreesCobblemon.MOD_ID, "apricorn"), ApricornFruit.TYPE);
        }
    }

}
