package com.dtteam.dtcobblemon.leaves;

import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.jetbrains.annotations.NotNull;

public class SaccharineLeavesProperties extends LeavesProperties {
    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(SaccharineLeavesProperties::new);

    public SaccharineLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override @NotNull
    protected DynamicLeavesBlock createDynamicLeaves(BlockBehaviour.Properties properties) {
        return new SaccharineLeavesBlock(this, properties);
    }
}