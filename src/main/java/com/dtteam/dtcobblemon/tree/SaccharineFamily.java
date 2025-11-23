package com.dtteam.dtcobblemon.tree;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.family.NetherFungusFamily;
import com.dtteam.dynamictrees.tree.species.Species;
import net.minecraft.resources.ResourceLocation;

public class SaccharineFamily extends Family {
    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(SaccharineFamily::new);

    public SaccharineFamily(ResourceLocation name) {
        super(name);
    }

}
