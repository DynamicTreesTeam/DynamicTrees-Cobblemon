package com.dtteam.dtcobblemon.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceLocation;

public class SaccharineSpecies extends Species {
    public static final TypedRegistry.EntryType<Species> TYPE = createDefaultType(SaccharineSpecies::new);

    public static double BeeNestWorldGenChance = 0.5;
    public static double BeeNestGrowChance = 0.1;

    public SaccharineSpecies(ResourceLocation name, Family family, LeavesProperties leavesProperties) {
        super(name, family, leavesProperties);
    }
}
