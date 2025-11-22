package com.dtteam.dtcobblemon.fruit;

import com.dtteam.dynamictrees.block.DynamicBlockProperties;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.fruit.FruitBlock;
import com.dtteam.dynamictrees.data.Generator;
import com.dtteam.dynamictrees.data.provider.DTBlockStateProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;

public class ApricornFruitStateGenerator implements Generator<DTBlockStateProvider, Fruit> {
    @Override
    public void generate(DTBlockStateProvider provider, Fruit input, Dependencies dependencies) {
        FruitBlock block = input.getBlock();
        provider.getVariantBuilder(block).forAllStates(
                state -> {
                    int maxAge = block.getMaxAge();
                    IntegerProperty ageProperty = DynamicBlockProperties.getOrCreateAge(maxAge);
                    int age = state.getValue(ageProperty);
                    if (age == maxAge) {
                        BlockModelBuilder builder = provider.models().withExistingParent(getName(block), provider.modLoc("apricorn_stage_" + age));
                        ResourceLocation texture = ResourceLocation.fromNamespaceAndPath(BuiltInRegistries.ITEM.getKey(input.getItemStack().getItem()).getNamespace(), "block/crops/" + getName(block));
                        builder = builder.texture("apricorn", texture);
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
        return BuiltInRegistries.BLOCK.getKey(item).getPath();
    }
}
