package com.dtteam.dtcobblemon.branch;

import com.dtteam.dynamictrees.block.branch.BasicBranchBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class SlatheredSaccharineBranchBlock extends BasicBranchBlock {

    public SlatheredSaccharineBranchBlock(ResourceLocation name, Properties blockProperties) {
        super(name, blockProperties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING);
        super.createBlockStateDefinition(builder);
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

}

//companion object {
//        const val HONEY_TYPE_MAX = 5
//
//val CODEC: MapCodec<SaccharineLogSlatheredBlock> = simpleCodec(::SaccharineLogSlatheredBlock)
//val HONEY_TYPE: IntegerProperty = IntegerProperty.create("honey_type", 0,HONEY_TYPE_MAX)
//
//fun createBehavior(): DispenseItemBehavior {
//    return DispenseItemBehavior { source, stack ->
//            val level = source.level
//        val pos = source.pos.relative(source.state.getValue(DispenserBlock.FACING))
//        val blockState = level.getBlockState(pos)
//
//        val waterBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER).item
//
//        if (blockState.block is SaccharineLogSlatheredBlock && stack.`is`(waterBottle)) {
//            val newState = CobblemonBlocks.SACCHARINE_LOG.defaultBlockState()
//                    .setValue(RotatedPillarBlock.AXIS, blockState.getValue(RotatedPillarBlock.AXIS))
//            SaccharineLogBlock.changeLogTypeDispenser(level, pos, newState, stack, source)
//        }
//        stack
//    }
//}
//    }
//
//init {
//    registerDefaultState(
//            stateDefinition.any()
//                    .setValue(FACING, Direction.NORTH)
//                    .setValue(RotatedPillarBlock.AXIS, Direction.Axis.Y)
//                    .setValue(HONEY_TYPE, 0)
//    )
//}
//
//override fun codec() = CODEC
//
//override fun useItemOn(stack: ItemStack, state: BlockState, level: Level, pos: BlockPos, player: Player, hand: InteractionHand, hitResult: BlockHitResult): ItemInteractionResult {
//    val itemStack = player.getItemInHand(hand)
//    val waterBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER).item
//    val blockFace = hitResult.direction
//
//    if (itemStack.`is`(waterBottle) && state.getValue(FACING) == blockFace) {
//        if (!level.isClientSide) {
//            // Replace the honey with the block variant
//            val newState = CobblemonBlocks.SACCHARINE_LOG.defaultBlockState()
//                    .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS))
//            level.playSound(null, pos, SoundEvents.GENERIC_SWIM, SoundSource.BLOCKS)
//            SaccharineLogBlock.changeLogType(level, pos, newState, player, itemStack)
//        }
//
//        spawnParticlesAtBlockFace(ParticleTypes.SPLASH, level, pos, blockFace, 40)
//        return ItemInteractionResult.SUCCESS
//    }
//    return super.useItemOn(stack, state, level, pos, player, hand, hitResult)
//}
//
//override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
//val direction = (context.player?.direction ?: Direction.NORTH).opposite
//        if (direction != Direction.UP && direction != Direction.DOWN) {
//val randomType = Random.nextInt(0, HONEY_TYPE_MAX + 1)
//            return defaultBlockState().setValue(FACING, direction).setValue(HONEY_TYPE, randomType)
//        }
//                return super.getStateForPlacement(context)
//    }
//
//override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, neighborBlock: Block, neighborPos: BlockPos,  movedByPiston: Boolean) {
//    val facingDirection = state.getValue(FACING)
//    val targetPos = pos.relative(facingDirection)
//
//    if (neighborPos == targetPos) {
//        val fluidState = level.getFluidState(neighborPos)
//        // Revert log to non-honey block if touching fluid
//        if (fluidState.amount > 3) {
//            val newState = CobblemonBlocks.SACCHARINE_LOG.defaultBlockState()
//                    .setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS))
//            level.playSound(null, pos, SoundEvents.GENERIC_SWIM, SoundSource.BLOCKS)
//            SaccharineLogBlock.changeLogType(level, pos, newState)
//        }
//    }
//
//    super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston)
//}
//
//override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
//    builder.add(FACING)
//    builder.add(RotatedPillarBlock.AXIS)
//    builder.add(HONEY_TYPE)
//}
//
//fun spawnParticlesAtBlockFace(particle:ParticleOptions, level: Level, pos: BlockPos, direction: Direction, amount: Int) {
//    val random = level.random
//    repeat(amount) {
//        val (posX, posY, posZ) = when (direction) {
//            Direction.UP -> Triple(pos.x + random.nextDouble(), pos.y + 1.0, pos.z + random.nextDouble())
//            Direction.DOWN -> Triple(pos.x + random.nextDouble(), pos.y + 0.0, pos.z + random.nextDouble())
//            Direction.NORTH -> Triple(pos.x + random.nextDouble(), pos.y + random.nextDouble(), pos.z + 0.0)
//            Direction.SOUTH -> Triple(pos.x + random.nextDouble(), pos.y + random.nextDouble(), pos.z + 1.0)
//            Direction.EAST -> Triple(pos.x + 1.0, pos.y + random.nextDouble(), pos.z + random.nextDouble())
//            Direction.WEST -> Triple(pos.x + 0.0, pos.y + random.nextDouble(), pos.z + random.nextDouble())
//                else -> null
//        } ?: return@repeat
//
//                level.addParticle(particle, posX, posY, posZ, 0.0, 0.0, 0.0)
//    }
//}
//}
