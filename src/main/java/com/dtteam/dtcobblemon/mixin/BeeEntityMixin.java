package com.dtteam.dtcobblemon.mixin;

import com.dtteam.dtcobblemon.leaves.DynamicSaccharineLeavesBlock;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.entity.animal.Bee$BeeGrowCropGoal")
public class BeeEntityMixin {

    @Unique
    private BlockState dtcobblemon$result = null;

    @Inject(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/BonemealableBlock;performBonemeal(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V", shift = At.Shift.BY, by = 2)
    )
    private void injectCustomGrowth(CallbackInfo ci, @Local(ordinal = 0) BlockState blockState, @Local Block block) {
        if (block instanceof DynamicSaccharineLeavesBlock) {

            int age = blockState.getValue(DynamicSaccharineLeavesBlock.AGE);
            boolean waterlogged = blockState.getValue(BlockStateProperties.WATERLOGGED);

            if (age < 2 && !waterlogged) {
                this.dtcobblemon$result = blockState.setValue(DynamicSaccharineLeavesBlock.AGE, age + 1);
            }
        }
    }

    @ModifyVariable(method = "tick", at = @At(value = "LOAD", ordinal = 0), index = 5)
    private BlockState applyCustomBlockState(BlockState before) {
        if(this.dtcobblemon$result != null && before == null) {
            BlockState result = this.dtcobblemon$result;
            this.dtcobblemon$result = null;
            return result;
        }

        return before;
    }


}
