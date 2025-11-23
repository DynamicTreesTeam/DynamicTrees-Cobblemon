package com.dtteam.dtcobblemon;

import com.dtteam.dtcobblemon.fruit.ApricornFruit;
import com.dtteam.dtcobblemon.tree.SaccharineFamily;
import com.dtteam.dtcobblemon.tree.SaccharineSpecies;
import com.dtteam.dynamictrees.api.registry.Registry;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.data.tags.DTBiomeTags;
import com.dtteam.dynamictrees.event.ApplierRegistryEvent;
import com.dtteam.dynamictrees.event.RegistryEvent;
import com.dtteam.dynamictrees.event.TypeRegistryEvent;
import com.dtteam.dynamictrees.systems.genfeature.BeeNestGenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatures;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.google.gson.JsonElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

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

    @SubscribeEvent
    public static void onGenFeatureRegistry(final RegistryEvent<GenFeature> event) {
        if (!event.isEntryOfType(GenFeature.class)) return;
        event.getRegistry().register(new BeeNestGenFeature(DynamicTreesCobblemon.Location("saccharine_bee_nest")){
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
