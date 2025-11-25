package com.dtteam.dtcobblemon.genfeature;

import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.dtteam.dynamictrees.api.configuration.ConfigurationProperty;
import com.dtteam.dynamictrees.systems.genfeature.BeeNestGenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SaccharineBeeNestGenFeature extends BeeNestGenFeature {

    public static final ConfigurationProperty<Float> COMBEE_PROBABILITY = ConfigurationProperty.floatProperty("combee_probability");
    public static final ConfigurationProperty<Integer> COMBEE_MIN_LEVEL = ConfigurationProperty.integer("combee_min_level");
    public static final ConfigurationProperty<Integer> COMBEE_MAX_LEVEL = ConfigurationProperty.integer("combee_max_level");
    private static final String PokemonArgs = "combee";
    public static double BeeNestWorldGenChance = 0.5;
    public static double BeeNestGrowChance = 0.1;

    public SaccharineBeeNestGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        this.register(COMBEE_PROBABILITY, COMBEE_MIN_LEVEL, COMBEE_MAX_LEVEL);
    }

    @Override @NotNull
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(NEST_BLOCK, Blocks.BEE_NEST)
                .with(MAX_HEIGHT, 32)
                .with(COMBEE_PROBABILITY, 0.5f)
                .with(COMBEE_MIN_LEVEL, 5)
                .with(COMBEE_MAX_LEVEL, 15)
                .with(CAN_GROW_PREDICATE, (world, pos) -> {
                    if (world.getRandom().nextFloat() > BeeNestGrowChance) return false;
                    for (BlockPos blockpos : BlockPos.betweenClosed(pos.below().north(2).west(2), pos.above().south(2).east(2))) {
                        if (world.getBlockState(blockpos).is(BlockTags.FLOWERS)) return true;
                    }
                    return false;
                })
                .with(WORLD_GEN_CHANCE_FUNCTION, (world, pos) -> BeeNestWorldGenChance)
                .with(MAX_COUNT, 1);
    }

    @Override
    protected boolean placeBeeNestInValidPlace(GenFeatureConfiguration configuration, LevelAccessor world, BlockPos rootPos, boolean worldGen, RandomSource random) {
        Block nestBlock = configuration.get(NEST_BLOCK);

        int treeHeight = getTreeHeight(world, rootPos, configuration.get(MAX_HEIGHT));
        if (nestAlreadyPresent(world, nestBlock, rootPos, treeHeight)) {
            return false;
        }
        List<Pair<BlockPos, List<Direction>>> validSpaces = findBranchPits(configuration, world, rootPos, treeHeight);
        if (validSpaces == null) {
            return false;
        }
        if (!validSpaces.isEmpty()) {
            Pair<BlockPos, List<Direction>> chosenSpace = validSpaces.get(world.getRandom().nextInt(validSpaces.size()));
            Direction chosenDir = chosenSpace.getValue().get(world.getRandom().nextInt(chosenSpace.getValue().size()));

            return placeBeeNestWithBees(configuration, world, nestBlock, chosenSpace.getKey(), chosenDir, worldGen, random);
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private boolean placeBeeNestWithBees(GenFeatureConfiguration configuration, LevelAccessor world, Block nestBlock, BlockPos pos, Direction faceDir, boolean worldGen, RandomSource random) {
        BlockState nestState = nestBlock.defaultBlockState();
        if (nestState.hasProperty(BeehiveBlock.FACING)) {
            nestState = nestState.setValue(BeehiveBlock.FACING, faceDir);
        }
        float combeeChance = configuration.get(COMBEE_PROBABILITY);
        int combeeMinLevel = configuration.get(COMBEE_MIN_LEVEL);
        int combeeMaxLevel = configuration.get(COMBEE_MAX_LEVEL);
        world.setBlock(pos, nestState, 3);
        world.getBlockEntity(pos, BlockEntityType.BEEHIVE).ifPresent((blockEntity) -> {
            int j = 2 + random.nextInt(2);
            float isCombee = random.nextFloat();
            for(int k = 0; k < j; ++k) {
                if (isCombee < combeeChance && world instanceof WorldGenRegion level){
                    String properties = PokemonArgs+" lvl="+(combeeMinLevel + random.nextInt(combeeMaxLevel-combeeMinLevel+1));
                    PokemonProperties pokemon = PokemonProperties.Companion.parse(properties);
                    PokemonEntity entity = pokemon.createEntity(level.getLevel());
                    blockEntity.addOccupant(entity);
                } else {
                    storeBee(random, blockEntity);
                }
            }

        });
        return true;
    }
}
