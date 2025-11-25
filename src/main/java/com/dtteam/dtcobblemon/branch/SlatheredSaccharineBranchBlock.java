package com.dtteam.dtcobblemon.branch;

import com.bedrockk.molang.runtime.value.DoubleValue;
import com.cobblemon.mod.common.entity.MoLangScriptingEntity;
import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.block.branch.BasicBranchBlock;
import com.dtteam.dynamictrees.tree.TreeHelper;
import com.dtteam.dynamictrees.tree.family.Family;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlatheredSaccharineBranchBlock extends BasicBranchBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public SlatheredSaccharineBranchBlock(ResourceLocation name, Properties blockProperties) {
        super(name, blockProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public int setRadius(LevelAccessor level, BlockPos pos, int radius, @Nullable Direction originDir, int flags) {
        BlockState currentState = level.getBlockState(pos);
        Direction honeyDirection = currentState.is(this) ?
                currentState.getValue(FACING) :
                Direction.Plane.HORIZONTAL.getRandomDirection(level.getRandom());
        return setRadius(level, pos, radius, flags, honeyDirection);
    }

    public int setRadius(LevelAccessor level, BlockPos pos, int radius, int flags, Direction honeyDirection) {
        destroyMode = DynamicTrees.DestroyMode.SET_RADIUS;
        boolean replacingWater = level.getBlockState(pos).getFluidState() == Fluids.WATER.getSource(false);
        boolean setWaterlogged = replacingWater && radius <= 7;

        BlockState newState = this.getStateForRadius(radius)
                .setValue(WATERLOGGED, setWaterlogged)
                .setValue(FACING, honeyDirection);
        level.setBlock(pos, newState, flags);

        destroyMode = DynamicTrees.DestroyMode.SLOPPY;
        return radius;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item waterBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER).getItem();
        Direction blockFace = hitResult.getDirection();

        if (itemStack.is(waterBottle) && state.getValue(FACING) == blockFace){
            if (!level.isClientSide){
                level.playSound(null, pos, SoundEvents.GENERIC_SWIM, SoundSource.BLOCKS);
                removeHoney(level, pos);
            }

            spawnParticlesAtBlockFace(ParticleTypes.SPLASH, level, pos, blockFace, 40);
            return ItemInteractionResult.SUCCESS;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidState) {
        boolean wasPlaced = super.placeLiquid(level, pos, state, fluidState);
        if (wasPlaced){
            removeHoney(level, pos);
        }
        return wasPlaced;
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        Direction facingDirection = state.getValue(FACING);
        BlockPos targetPos = pos.relative(facingDirection);

        if (neighborPos.equals(targetPos)){
            FluidState fluidState = level.getFluidState(neighborPos);
            if (fluidState.getAmount() > 3){
                level.playSound(null, pos, SoundEvents.GENERIC_SWIM, SoundSource.BLOCKS);
                removeHoney(level, pos);
            }
        }

        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
    }

    private void removeHoney(LevelAccessor level, BlockPos pos) {
        Family family = getFamily();
        int rad = TreeHelper.getRadius(level, pos);
        if (rad > 0 && family.getBranch().isPresent()){
            SaccharineBranchBlock.changeLogType(level, pos, family.getBranch().get(), rad, null);
        }
    }

    public void spawnParticlesAtBlockFace(ParticleOptions particle, Level level, BlockPos pos, Direction direction, int amount) {
        RandomSource random = level.random;

        for (int i = 0; i < amount; i++) {
            double posX, posY, posZ;

            switch (direction) {
                case UP:
                    posX = pos.getX() + random.nextDouble();
                    posY = pos.getY() + 1.0;
                    posZ = pos.getZ() + random.nextDouble();
                    break;
                case DOWN:
                    posX = pos.getX() + random.nextDouble();
                    posY = pos.getY();
                    posZ = pos.getZ() + random.nextDouble();
                    break;
                case NORTH:
                    posX = pos.getX() + random.nextDouble();
                    posY = pos.getY() + random.nextDouble();
                    posZ = pos.getZ();
                    break;
                case SOUTH:
                    posX = pos.getX() + random.nextDouble();
                    posY = pos.getY() + random.nextDouble();
                    posZ = pos.getZ() + 1.0;
                    break;
                case EAST:
                    posX = pos.getX() + 1.0;
                    posY = pos.getY() + random.nextDouble();
                    posZ = pos.getZ() + random.nextDouble();
                    break;
                case WEST:
                    posX = pos.getX();
                    posY = pos.getY() + random.nextDouble();
                    posZ = pos.getZ() + random.nextDouble();
                    break;
                default:
                    continue;
            }

            level.addParticle(particle, posX, posY, posZ, 0.0, 0.0, 0.0);
        }
    }

    @Override @NotNull
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext eContext && TreeHelper.getRadius(level, pos) < 8){
            Entity entity = eContext.getEntity();
            if (entity instanceof MoLangScriptingEntity moEntity && moEntity.getConfig().getMap().getOrDefault("can_path_through_sacc_leaves", DoubleValue.ZERO).asDouble() == 1.0){
                return Shapes.empty();
            }
        }
        return super.getCollisionShape(state, level, pos, context);
    }

}

//https://gitlab.com/cable-mc/cobblemon/-/blob/main/common/src/main/kotlin/com/cobblemon/mod/common/block/SaccharineLogSlatheredBlock.kt