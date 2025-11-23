package com.dtteam.dtcobblemon.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatures;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;

import static com.dtteam.dynamictrees.systems.genfeature.BeeNestGenFeature.WORLD_GEN_CHANCE_FUNCTION;
import static com.dtteam.dynamictrees.systems.genfeature.GenFeature.CAN_GROW_PREDICATE;

public class SaccharineSpecies extends Species {
    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(SaccharineSpecies::new);

    public static double BeeNestWorldGenChance = 0.5;
    public static double BeeNestGrowChance = 0.1;

    public SaccharineSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }
}
