package com.dtteam.dtcobblemon.mixin;

import com.cobblemon.mod.common.entity.pokemon.ai.sensors.SacLeavesSensor;
import com.dtteam.dtcobblemon.leaves.DynamicSaccharineLeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SacLeavesSensor.class)
public class SacLeavesSensorMixin {

    @Inject(method = "isValidLeafBlock", at = @At("HEAD"), cancellable = true, remap = false)
    private void isValidLeafBlock(BlockState state, CallbackInfoReturnable<Boolean> cir){
        if (state.getBlock() instanceof DynamicSaccharineLeavesBlock){
            if (state.hasProperty(BlockStateProperties.WATERLOGGED)
                    && state.getValue(BlockStateProperties.WATERLOGGED)) {
                cir.setReturnValue(false);
            }
            cir.setReturnValue(state.getValue(DynamicSaccharineLeavesBlock.HONEY) != DynamicSaccharineLeavesBlock.MAX_HONEY);
        }
    }

}
