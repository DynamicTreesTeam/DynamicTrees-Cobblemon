package com.dtteam.dtcobblemon.genfeature;

import com.dtteam.dtcobblemon.tree.SaccharineSpecies;
import com.dtteam.dynamictrees.api.configuration.ConfigurationProperty;
import com.dtteam.dynamictrees.systems.genfeature.BeeNestGenFeature;
import com.dtteam.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class SaccharineBeeNestGenFeature extends BeeNestGenFeature {

    public static final ConfigurationProperty<Integer> COMBEE_CHANCE = ConfigurationProperty.integer("combee_chance");

    public SaccharineBeeNestGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        super.registerProperties();
        this.register(COMBEE_CHANCE);
    }

    @Override @NotNull
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(NEST_BLOCK, Blocks.BEE_NEST)
                .with(MAX_HEIGHT, 32)
                .with(COMBEE_CHANCE, 2)
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

    //TODO: later, it crashes on Cobblemon's side for some reason.
//    @Override
//    @SuppressWarnings("deprecation")
//    protected boolean placeBeeNestWithBees(LevelAccessor world, Block nestBlock, BlockPos pos, Direction faceDir, boolean worldGen, RandomSource random) {
//        BlockState nestState = nestBlock.defaultBlockState();
//        if (nestState.hasProperty(BeehiveBlock.FACING)) {
//            nestState = nestState.setValue(BeehiveBlock.FACING, faceDir);
//        }
//        world.setBlock(pos, nestState, 3);
//        world.getBlockEntity(pos, BlockEntityType.BEEHIVE).ifPresent((blockEntity) -> {
//            int j = 2 + random.nextInt(2);
//            int isCombee = random.nextInt(2);
//            for(int k = 0; k < j; ++k) {
//                if (isCombee == 0 && world instanceof WorldGenRegion level){
//                    String properties = "${POKEMON_ARGS} lvl=${LEVEL_RANGE.random()}";
//                    PokemonProperties pokemon = PokemonProperties.Companion.parse(properties);
//                    PokemonEntity entity = pokemon.createEntity(level.getLevel());
//                    blockEntity.addOccupant(entity);
//                } else {
//                    storeBee(random, blockEntity);
//                }
//            }
//
//        });
//        return true;
//    }
}
