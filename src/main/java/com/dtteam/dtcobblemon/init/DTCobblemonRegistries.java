package com.dtteam.dtcobblemon.init;

import com.dtteam.dtcobblemon.DynamicTreesCobblemon;
import com.dtteam.dtcobblemon.fruit.ApricornFruit;
import com.dtteam.dtcobblemon.leaves.SaccharineLeavesProperties;
import com.dtteam.dtcobblemon.tree.SaccharineFamily;
import com.dtteam.dtcobblemon.tree.SaccharineSpecies;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.event.RegistryEvent;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.systems.genfeature.BeeNestGenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

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
        event.getRegistry().register(new BeeNestGenFeature(DynamicTreesCobblemon.location("saccharine_bee_nest")){
            @Override @NotNull
            public GenFeatureConfiguration createDefaultConfiguration() {
                return super.createDefaultConfiguration()
                        .with(NEST_BLOCK, Blocks.BEE_NEST)
                        .with(MAX_HEIGHT, 32)
                        .with(CAN_GROW_PREDICATE, (world, pos) -> {
                            if (world.getRandom().nextFloat() > SaccharineSpecies.BeeNestGrowChance) {
                                return false;
                            }
                            // Default flower check predicate, straight from AbstractTreeGrower
                            for (BlockPos blockpos : BlockPos.betweenClosed(pos.below().north(2).west(2), pos.above().south(2).east(2))) {
                                if (world.getBlockState(blockpos).is(BlockTags.FLOWERS)) {
                                    return true;
                                }
                            }
                            return false;
                        })
                        .with(WORLD_GEN_CHANCE_FUNCTION, (world, pos) -> SaccharineSpecies.BeeNestWorldGenChance)
                        .with(MAX_COUNT, 1);
            }
        });
    }



}
