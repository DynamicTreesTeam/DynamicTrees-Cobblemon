package com.dtteam.dtcobblemon.init;

import com.dtteam.dtcobblemon.DynamicTreesCobblemon;
import com.dtteam.dtcobblemon.fruit.ApricornFruit;
import com.dtteam.dtcobblemon.genfeature.SaccharineBeeNestGenFeature;
import com.dtteam.dtcobblemon.leaves.DTCobblemonCellKits;
import com.dtteam.dtcobblemon.leaves.SaccharineLeavesProperties;
import com.dtteam.dtcobblemon.tree.SaccharineFamily;
import com.dtteam.dtcobblemon.tree.SaccharineSpecies;
import com.dtteam.dynamictrees.api.cell.CellKit;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.event.RegistryEvent;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.systems.cell.CellKits;
import com.dtteam.dynamictrees.systems.genfeature.BeeNestGenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@EventBusSubscriber(modid = DynamicTreesCobblemon.MOD_ID)
public class DTCobblemonRegistries {

    @SubscribeEvent
    public static void registerFruitTypes(TypeRegistryEvent<Fruit> event) {
        if (event.isEntryOfType(Fruit.class)){
            event.registerType(DynamicTreesCobblemon.location("apricorn"), ApricornFruit.TYPE);
        }
    }

    @SubscribeEvent
    public static void registerSpeciesTypes(TypeRegistryEvent<Species> event) {
        if (event.isEntryOfType(Species.class)){
            event.registerType(DynamicTreesCobblemon.location("saccharine"), SaccharineSpecies.TYPE);
        }
    }

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
