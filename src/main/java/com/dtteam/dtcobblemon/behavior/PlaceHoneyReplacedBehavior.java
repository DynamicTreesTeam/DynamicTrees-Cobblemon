package com.dtteam.dtcobblemon.behavior;

import com.cobblemon.mod.common.CobblemonActivities;
import com.cobblemon.mod.common.CobblemonBlocks;
import com.cobblemon.mod.common.CobblemonMemories;
import com.cobblemon.mod.common.api.ai.config.task.PlaceHoneyInSaccLeavesTaskConfig;
import com.cobblemon.mod.common.block.SaccharineLeafBlock;
import com.dtteam.dtcobblemon.leaves.DynamicSaccharineLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

//So sorry about overriding the whole behavior but there was really no other way to do it...
public class PlaceHoneyReplacedBehavior extends Behavior<LivingEntity> {

    public PlaceHoneyReplacedBehavior(Behavior<LivingEntity> original) {
        super(original.entryCondition, original.minDuration, original.maxDuration);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, LivingEntity owner) {
        if (level.isNight() || level.isRaining()) return false;
        boolean hasNectar = owner.getBrain()
                .getMemory(CobblemonMemories.HAS_NECTAR)
                .orElse(false);

        if (!hasNectar) return false;

        Optional<BlockPos> opt = owner.getBrain().getMemory(CobblemonMemories.NEARBY_SACC_LEAVES);
        if (opt.isEmpty()) return false;

        BlockPos pos = opt.get();
        if (!isValidLeaves(level, pos))
            return false;

        return Vec3.atCenterOf(pos).distanceTo(owner.position()) <= 1.2;
    }

    private static boolean isValidLeaves(ServerLevel level, BlockPos pos) {
        Block block = level.getBlockState(pos).getBlock();
        return block == CobblemonBlocks.SACCHARINE_LEAVES ||
                block instanceof DynamicSaccharineLeavesBlock;
    }

    private int successfulPollinationTicks = 0;
    private int lastSoundPlayedTick = 0;
    private Vec3 hoverPos = null;

    @Override
    protected boolean canStillUse(ServerLevel level, LivingEntity owner, long gameTime) {
        return checkExtraStartConditions(level, owner)
                && owner.getBrain().activeActivities.contains(CobblemonActivities.POKEMON_POLLINATION);
    }

    @Override
    protected void start(ServerLevel level, LivingEntity owner, long gameTime) {
        super.start(level, owner, gameTime);
        hoverPos = null;
    }

    @Override
    protected void tick(ServerLevel level, LivingEntity entity, long gameTime) {
        successfulPollinationTicks++;

        if (!(entity instanceof Mob owner)) return;

        // Drip sound
        if (owner.getRandom().nextFloat() < 0.05f
                && successfulPollinationTicks > lastSoundPlayedTick + 60) {

            lastSoundPlayedTick = successfulPollinationTicks;
            owner.playSound(SoundEvents.BEEHIVE_DRIP, 1.0f, 1.0f);
        }

        BlockPos leavesPos = owner.getBrain()
                .getMemory(CobblemonMemories.NEARBY_SACC_LEAVES)
                .orElse(null);

        boolean reachedHover = (hoverPos == null)
                || owner.position().distanceTo(hoverPos) <= 0.1;

        boolean randomMove = owner.getRandom().nextInt(40) == 0;

        if (reachedHover && randomMove) {
            // Mimic vanilla bee wriggling
            if (hoverPos == null) {
                hoverPos = leavesPos != null
                        ? Vec3.atBottomCenterOf(leavesPos).add(0.0, 0.4, 0.0)
                        : null;
            }

            if (leavesPos != null) {
                RandomSource rnd = owner.getRandom();

                hoverPos = Vec3.atBottomCenterOf(leavesPos).add(
                        (rnd.nextDouble() * 2 - 1) * (1.0 / 5.0),
                        0.3 + (rnd.nextDouble() * 2 - 1) * (1.0 / 7.0),
                        (rnd.nextDouble() * 2 - 1) * (1.0 / 5.0)
                );

                owner.getMoveControl().setWantedPosition(
                        hoverPos.x(),
                        hoverPos.y(),
                        hoverPos.z(),
                        0.35
                );

                owner.getLookControl().setLookAt(
                        Vec3.atBottomCenterOf(leavesPos).add(0.0, 0.5, 0.0)
                );
            }
        }
        else if (!reachedHover && hoverPos != null) {
            owner.getMoveControl().setWantedPosition(
                    hoverPos.x(),
                    hoverPos.y(),
                    hoverPos.z(),
                    0.35
            );
        }
    }

    @Override
    protected void stop(ServerLevel level, LivingEntity owner, long gameTime) {

        if (successfulPollinationTicks > PlaceHoneyInSaccLeavesTaskConfig.REQUIRED_SUCCESSFUL_POLLINATION_TICKS) {
            BlockPos blockPos = owner.getBrain()
                    .getMemory(CobblemonMemories.NEARBY_SACC_LEAVES)
                    .orElse(null);

            if (blockPos != null) {
                BlockState state = level.getBlockState(blockPos);
                if (state.hasProperty(BlockStateProperties.AGE_2)) {

                    int age = state.getValue(BlockStateProperties.AGE_2);

                    if (age < SaccharineLeafBlock.MAX_AGE) {
                        owner.getBrain().eraseMemory(CobblemonMemories.HAS_NECTAR);

                        BlockState newState = state.setValue(
                                BlockStateProperties.AGE_2, age + 1
                        );

                        level.setBlock(blockPos, newState, 3);
                    }
                }
            }
        }

        successfulPollinationTicks = 0;
        owner.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }
}