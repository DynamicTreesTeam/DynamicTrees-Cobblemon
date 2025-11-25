package com.dtteam.dtcobblemon.init;

import com.dtteam.dtcobblemon.DynamicTreesCobblemon;
import com.dtteam.dtcobblemon.fruit.ApricornFruit;
import com.dtteam.dtcobblemon.genfeature.SaccharineBeeNestGenFeature;
import com.dtteam.dtcobblemon.leaves.DTCobblemonCellKits;
import com.dtteam.dtcobblemon.leaves.SaccharineLeavesProperties;
import com.dtteam.dtcobblemon.tree.SaccharineFamily;
import com.dtteam.dynamictrees.api.cell.CellKit;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.event.RegistryEvent;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.tree.family.Family;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = DynamicTreesCobblemon.MOD_ID)
public class DTCobblemonRegistries {

    @SubscribeEvent
    public static void registerFruitTypes(TypeRegistryEvent<Fruit> event) {
        if (event.isEntryOfType(Fruit.class)){
            event.registerType(DynamicTreesCobblemon.location("apricorn"), ApricornFruit.TYPE);
        }
    }

//    @SubscribeEvent
//    public static void registerSpeciesTypes(TypeRegistryEvent<Species> event) {
//        if (event.isEntryOfType(Species.class)){
//
//        }
//    }

    @SubscribeEvent
    public static void registerFamilyTypes(TypeRegistryEvent<Family> event) {
        if (event.isEntryOfType(Family.class)){
            event.registerType(DynamicTreesCobblemon.location("saccharine"), SaccharineFamily.TYPE);
        }
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes(TypeRegistryEvent<LeavesProperties> event) {
        if (event.isEntryOfType(LeavesProperties.class)){
            event.registerType(DynamicTreesCobblemon.location("saccharine"), SaccharineLeavesProperties.TYPE);
        }
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry(final RegistryEvent<GenFeature> event) {
        if (!event.isEntryOfType(GenFeature.class)) return;
        event.getRegistry().register(new SaccharineBeeNestGenFeature(DynamicTreesCobblemon.location("saccharine_bee_nest")));
    }

    @SubscribeEvent
    public static void onCellKitRegistry(final RegistryEvent<CellKit> event) {
        if (!event.isEntryOfType(CellKit.class)) return;
        DTCobblemonCellKits.register(event.getRegistry());
    }

}
