package com.dtteam.dtcobblemon;

import com.dtteam.dtcobblemon.fruit.ApricornFruit;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = DynamicTreesCobblemon.MOD_ID)
public class DTCobblemonRegistries {

    @SubscribeEvent
    public static void registryEvent(TypeRegistryEvent<Fruit> event) {
        if (event.isEntryOfType(Fruit.class)){
            event.registerType(ResourceLocation.fromNamespaceAndPath(DynamicTreesCobblemon.MOD_ID, "apricorn"), ApricornFruit.TYPE);
        }
    }

}
