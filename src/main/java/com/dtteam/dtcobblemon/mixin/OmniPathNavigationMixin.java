package com.dtteam.dtcobblemon.mixin;

import com.cobblemon.mod.common.entity.ai.OmniPathNavigation;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.dtteam.dtcobblemon.leaves.DynamicSaccharineLeavesBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(OmniPathNavigation.class)
public class OmniPathNavigationMixin {

    @Final @Shadow
    private Level world;

    @Final @Shadow
    private Mob entity;

    @Final @Shadow
    public Path findPath(BlockPos target, int distance){ return null; }

    @Inject(method = "createPath(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/pathfinder/Path;", at = @At(value = "RETURN"), cancellable = true, remap = false)
    private void createPath(BlockPos target, int distance, CallbackInfoReturnable<Path> cir){
        BlockState blockState = world.getBlockState(target);

        if (blockState.getBlock() instanceof DynamicSaccharineLeavesBlock &&
                entity instanceof PokemonEntity pokemonEntity && pokemonEntity.canPathThroughSaccLeaves())
        {
            cir.setReturnValue(findPath(target, distance));
        }
    }

    @Inject(method = "getGroundY", at = @At(value = "RETURN"), cancellable = true, remap = false)
    private void getGroundY(Vec3 vec, CallbackInfoReturnable<Double> cir){
        BlockPos blockPos = BlockPos.containing(vec);
        if (world.getBlockState(blockPos).getBlock() instanceof DynamicSaccharineLeavesBlock &&
                entity instanceof PokemonEntity pokemonEntity && pokemonEntity.canPathThroughSaccLeaves())
        {
            cir.setReturnValue(vec.y + 0.5);
        }
    }

}
