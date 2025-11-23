package com.dtteam.dtcobblemon.leaves;

import com.bedrockk.molang.runtime.value.DoubleValue;
import com.cobblemon.mod.common.entity.MoLangScriptingEntity;
import com.dtteam.dynamictrees.block.leaves.DynamicLeavesBlock;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.tree.ChunkTreeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicSaccharineLeavesBlock extends DynamicLeavesBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
    public static final int MAX_AGE = 2;
    public static final int MIN_AGE = 0;

    public DynamicSaccharineLeavesBlock(LeavesProperties leavesProperties, Properties properties) {
        super(leavesProperties, properties);
        registerDefaultState(defaultBlockState().setValue(AGE, MIN_AGE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public int updateHydro(LevelAccessor accessor, BlockPos pos, BlockState state, boolean worldGen) {
        LeavesProperties leavesProperties = this.getLeavesProperties();
        int oldHydro = state.getValue(DISTANCE);
        if (!ChunkTreeHelper.canCheckSurroundings(accessor, pos, 2)) {
            return oldHydro;
        } else {
            int newHydro = this.getHydrationLevelFromNeighbors(accessor, pos, leavesProperties);
            if (oldHydro != newHydro) {
                BlockState placeState = getLeavesBlockStateForPlacementWithHoney(accessor, pos, state, leavesProperties.getDynamicLeavesState(newHydro), worldGen, oldHydro);
                boolean decayed = newHydro == 0;
                int flag = decayed ? 3 : (this.appearanceChangesWithHydro(oldHydro, newHydro) ? 2 : 4);
                accessor.setBlock(pos, placeState, flag);
            }

            return newHydro;
        }
    }

    private @NotNull BlockState getLeavesBlockStateForPlacementWithHoney(LevelAccessor accessor, BlockPos pos, BlockState oldState,BlockState newHydroState, boolean worldGen, int oldHydro) {
        int honey = oldState.getValue(AGE);
        if (newHydroState.hasProperty(AGE)) newHydroState = newHydroState.setValue(AGE, honey);
        return this.getLeavesBlockStateForPlacement(accessor, pos, newHydroState, oldHydro, worldGen);
    }


    @Override @NotNull
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext eContext){
            Entity entity = eContext.getEntity();
            if (entity instanceof MoLangScriptingEntity moEntity && moEntity.getConfig().getMap().getOrDefault("can_path_through_sacc_leaves", DoubleValue.ZERO).asDouble() == 1.0){
                return Shapes.empty();
            }
        }
        return super.getCollisionShape(state, level, pos, context);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        int currentAge = state.getValue(AGE);

        if (currentAge > MIN_AGE && random.nextInt(2) == 0) {
            for (int i = 1; i <= 10; i++) {
                BlockPos belowPos = pos.below(i);
                BlockState belowState = world.getBlockState(belowPos);

                if (!belowState.isAir()) {
                    if (belowState.getBlock() instanceof DynamicSaccharineLeavesBlock) {
                        int belowAge = belowState.getValue(AGE);
                        if (belowAge < MAX_AGE) {
                            world.setBlock(pos, changeAge(state, -1), Block.UPDATE_CLIENTS);
                            world.setBlock(belowPos, changeAge(belowState, 1), Block.UPDATE_CLIENTS);
                        }
                    }
                    break;
                }
            }
        }

        super.randomTick(state, world, pos, random);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.getValue(WATERLOGGED) && fluidState.getType() == Fluids.WATER) {
            boolean hasHoney = state.getValue(AGE) > 0;

            if (!level.isClientSide()) {
                BlockState newState = state.setValue(WATERLOGGED, true);
                if (hasHoney) newState = newState.setValue(AGE, 0);

                level.setBlock(pos, newState, 3);
                level.scheduleTick(pos, fluidState.getType(), fluidState.getType().getTickDelay(level));
            }

            if (hasHoney && level instanceof Level lvl) {
                spawnDestroyHoneyParticles(lvl, pos, state);
            }

            return true;
        }
        return false;
    }

    @Override
    @Deprecated
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        int particleCount = random.nextInt(3);

        if (state.getValue(AGE) == 1) {
            for (int i = 0; i < particleCount; i++) {
                spawnHoneyParticles(level, pos, state, 0.025F);
            }
        } else if (state.getValue(AGE) == 2) {
            for (int i = 0; i < particleCount; i++) {
                spawnHoneyParticles(level, pos, state, 0.05F);
            }
        }
    }

    private void spawnHoneyParticles(Level level, BlockPos pos, BlockState state, float rate) {
        if (state.getFluidState().isEmpty() && level.random.nextFloat() < rate) {
            VoxelShape shape = state.getCollisionShape(level, pos);
            double d = shape.max(Direction.Axis.Y);

            if (d >= 1.0 && !state.is(BlockTags.IMPERMEABLE)) {
                double e = shape.min(Direction.Axis.Y);

                if (e > 0.0) {
                    addHoneyParticle(level, pos, shape, pos.getY() + e - 0.05);
                } else {
                    BlockPos below = pos.below();
                    BlockState belowState = level.getBlockState(below);
                    VoxelShape shape2 = belowState.getCollisionShape(level, below);
                    double f = shape2.max(Direction.Axis.Y);

                    if ((f < 1.0 || !belowState.isSolidRender(level, below)) && belowState.getFluidState().isEmpty()) {
                        addHoneyParticle(level, pos, shape, pos.getY() - 0.05);
                    }
                }
            }
        }
    }

    private void addHoneyParticle(Level level, BlockPos pos, VoxelShape shape, double height) {
        addHoneyParticle(level,
                pos.getX() + shape.min(Direction.Axis.X),
                pos.getX() + shape.max(Direction.Axis.X),
                pos.getZ() + shape.min(Direction.Axis.Z),
                pos.getZ() + shape.max(Direction.Axis.Z),
                height);
    }

    private void addHoneyParticle(Level level, double minX, double maxX, double minZ, double maxZ, double height) {
        level.addParticle(
                ParticleTypes.DRIPPING_HONEY,
                Mth.lerp(level.random.nextDouble(), minX, maxX),
                height,
                Mth.lerp(level.random.nextDouble(), minZ, maxZ),
                0.0, 0.0, 0.0
        );
    }

    @Override
    public boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override @NotNull
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!state.getValue(WATERLOGGED)) {
            ItemStack item = player.getItemInHand(hand);

            boolean isGlassBottle = item.is(Items.GLASS_BOTTLE);
            boolean isHoneyBottle = item.is(Items.HONEY_BOTTLE);

            if (isGlassBottle && !isAtMinAge(state)) {
                item.consume(1, player);
                player.addItem(new ItemStack(Items.HONEY_BOTTLE));

                level.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS);
                level.setBlock(pos, state.setValue(AGE, 0), 2);
                level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);

                return ItemInteractionResult.SUCCESS;
            } else if (isHoneyBottle && !isAtMaxAge(state)) {
                item.consume(1, player);

                level.playSound(null, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS);
                level.setBlock(pos, state.setValue(AGE, 2), 2);
                level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);

                return ItemInteractionResult.SUCCESS;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    private boolean isAtMaxAge(BlockState state) {
        return state.getValue(AGE) == MAX_AGE;
    }

    private boolean isAtMinAge(BlockState state) {
        return state.getValue(AGE) == MIN_AGE;
    }

    private BlockState changeAge(BlockState state, int value) {
        int newAge = Mth.clamp(state.getValue(AGE) + value, MIN_AGE, MAX_AGE);
        return state.setValue(AGE, newAge);
    }

    private void spawnDestroyHoneyParticles(Level level, BlockPos pos, BlockState state) {
        if (!isAtMinAge(state)) {
            int amount = isAtMaxAge(state) ? 30 : 10;

            for (int i = 0; i < amount; i++) {
                double ox = (level.random.nextDouble() * 1.2) - 0.6;
                double oy = (level.random.nextDouble() * 1.2) - 0.6;
                double oz = (level.random.nextDouble() * 1.2) - 0.6;

                level.addParticle(
                        ParticleTypes.FALLING_HONEY,
                        pos.getX() + 0.5 + ox,
                        pos.getY() + 0.5 + oy,
                        pos.getZ() + 0.5 + oz,
                        0.0, 1.0, 0.0
                );
            }
        }
    }

    @Override
    public void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        spawnDestroyHoneyParticles(level, pos, state);
        super.spawnDestroyParticles(level, player, pos, state.setValue(AGE, state.getValue(AGE)));
    }
}

//Original code from Cobblemon, trying to copy this behaviour as close as possible.
//https://gitlab.com/cable-mc/cobblemon/-/blob/main/common/src/main/kotlin/com/cobblemon/mod/common/block/SaccharineLeafBlock.kt