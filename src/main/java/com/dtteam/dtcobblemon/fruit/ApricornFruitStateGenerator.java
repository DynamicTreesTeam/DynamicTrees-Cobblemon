package com.dtteam.dtcobblemon.fruit;

import com.ferreusveritas.dynamictrees.api.data.Generator;
import com.ferreusveritas.dynamictrees.block.FruitBlock;
import com.ferreusveritas.dynamictrees.data.provider.DTBlockStateProvider;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.util.AgeProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.registries.ForgeRegistries;

public class ApricornFruitStateGenerator implements Generator<DTBlockStateProvider, Fruit> {
    @Override
    public void generate(DTBlockStateProvider provider, Fruit input, Dependencies dependencies) {
        FruitBlock block = input.getBlock();
        provider.getVariantBuilder(block).forAllStates(
                state -> {
                    int maxAge = block.getMaxAge();
                    IntegerProperty ageProperty = AgeProperties.getOrCreate(maxAge);
                    int age = state.getValue(ageProperty);
                    if (age == maxAge) {
                        BlockModelBuilder builder = provider.models().withExistingParent(getName(block), provider.modLoc("apricorn_stage_" + age));
                        ResourceLocation texture = new ResourceLocation(ForgeRegistries.ITEMS.getKey(input.getItemStack().getItem()).getNamespace(), "block/" + getName(block));
                        builder = builder.texture("0", texture).texture("particle", texture);
                        builder = builder.texture("particle", texture);
                        return ConfiguredModel.builder().modelFile(builder).build();
                    }
                    return ConfiguredModel.builder().modelFile(provider.models().getExistingFile(provider.modLoc("apricorn_stage_" + age))).build();
                }
        );

    }

    @Override
    public Dependencies gatherDependencies(Fruit input) {
        return new Dependencies();
    }

    protected String getName(Block item) {
        return ForgeRegistries.BLOCKS.getKey(item).getPath();
    }
}
