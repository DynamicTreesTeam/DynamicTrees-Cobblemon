package com.dtteam.dtcobblemon.datagen;

import com.dtteam.dtcobblemon.DTCobblemon;
import com.ferreusveritas.dynamictrees.block.FruitBlock;
import com.ferreusveritas.dynamictrees.util.AgeProperties;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput generator, ExistingFileHelper existingFileHelper) {
        super(generator, DTCobblemon.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        ForgeRegistries.BLOCKS.getValues().stream().filter(
                block -> ForgeRegistries.BLOCKS.getKey(block).getNamespace().equals(DTCobblemon.MODID)
                        && !ForgeRegistries.BLOCKS.getKey(block).getPath().contains("apricorn")
        ).forEach(
                block -> {
                    getVariantBuilder(block).forAllStates(
                            state -> {
                                int maxAge = ((FruitBlock) block).getMaxAge();
                                IntegerProperty ageProperty = AgeProperties.getOrCreate(maxAge);
                                int age = state.getValue(ageProperty);
                                String texturePath = age < maxAge ? "block/apricorn_stage_" + age : getName(block);
                                ResourceLocation texture = new ResourceLocation(
                                        age < maxAge ? ForgeRegistries.BLOCKS.getKey(block).getNamespace() : "cobblemon", texturePath);
                                return ConfiguredModel.builder().modelFile(models()
                                        .withExistingParent(getName(block), modLoc("apricorn_stage_" + age))
                                        .texture("0", texture)
                                        .texture("particle", texture)
                                ).build();
                            }
                    );
                }
        );

    }

    protected void simpleCubeBottomTopBlockState(Block block) {
        simpleBlock(block, blockCubeTopModel(block));
    }

    protected BlockModelBuilder blockCubeTopModel(Block block) {
        String name = getName(block);
        return models().cubeBottomTop(name, modLoc("block/" + name + "_side"), modLoc("block/" + name + "_bottom"), modLoc("block/" + name + "_top"));
    }

    protected String getName(Block item) {
        return ForgeRegistries.BLOCKS.getKey(item).getPath();
    }
}