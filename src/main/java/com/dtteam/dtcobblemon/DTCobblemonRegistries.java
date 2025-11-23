package com.dtteam.dtcobblemon;

import com.dtteam.dtcobblemon.fruit.ApricornFruit;
import com.dtteam.dtcobblemon.tree.SaccharineFamily;
import com.dtteam.dtcobblemon.tree.SaccharineSpecies;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.event.ApplierRegistryEvent;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatures;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import static com.dtteam.dynamictrees.systems.genfeature.BeeNestGenFeature.WORLD_GEN_CHANCE_FUNCTION;
import static com.dtteam.dynamictrees.systems.genfeature.GenFeature.CAN_GROW_PREDICATE;

@EventBusSubscriber(modid = DynamicTreesCobblemon.MOD_ID)
public class DTCobblemonRegistries {

    @SubscribeEvent
    public static void registerFruitTypes(TypeRegistryEvent<Fruit> event) {
        if (event.isEntryOfType(Fruit.class)){
            event.registerType(DynamicTreesCobblemon.Location("apricorn"), ApricornFruit.TYPE);
        }
    }

    @SubscribeEvent
    public static void registerSpeciesTypes(TypeRegistryEvent<Species> event) {
        if (event.isEntryOfType(Species.class)){
            event.registerType(DynamicTreesCobblemon.Location("saccharine"), SaccharineSpecies.TYPE);
        }
    }

    @SubscribeEvent
    public static void registerFamilyTypes(TypeRegistryEvent<Family> event) {
        if (event.isEntryOfType(Family.class)){
            event.registerType(DynamicTreesCobblemon.Location("saccharine"), SaccharineFamily.TYPE);
        }
    }

}
