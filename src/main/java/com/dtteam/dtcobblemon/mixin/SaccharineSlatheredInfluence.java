package com.dtteam.dtcobblemon.mixin;

import com.cobblemon.mod.common.api.spawning.detail.SpawnAction;
import com.cobblemon.mod.common.api.spawning.influence.SaccharineLogSlatheredInfluence;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;
import com.cobblemon.mod.common.net.messages.client.effect.SaccharineLogBlockParticlesPacket;
import com.dtteam.dtcobblemon.branch.SlatheredSaccharineBranchBlock;
import com.dtteam.dynamictrees.tree.TreeHelper;
import kotlin.jvm.functions.Function1;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaccharineLogSlatheredInfluence.class)
public class SaccharineSlatheredInfluence {

    @Unique
    private boolean dynamicTrees_Cobblemon_1_21_1$activated;

    @Final @Shadow
    private BlockPos pos;

    @Final @Shadow
    public BlockPos attemptSafeMove(ServerLevel level, PokemonEntity entity, BlockPos pos, Direction direction){ return null; }

    @Inject(method = "affectSpawn", at = @At("TAIL"), remap = false)
    private void affectSpawn(SpawnAction<?> action, Entity entity, CallbackInfo ci){
        if (entity instanceof PokemonEntity pokemonEntity && !dynamicTrees_Cobblemon_1_21_1$activated) {
            BlockPos logPos = this.pos;
            ServerLevel level = action.getSpawnablePosition().getWorld().getLevel();

            if (logPos != null) {
                BlockState blockState = level.getBlockState(logPos);

                if (blockState.getBlock() instanceof SlatheredSaccharineBranchBlock branch) {

                    Direction direction = blockState.getValue(HorizontalDirectionalBlock.FACING);
                    BlockPos safePos = attemptSafeMove(level, pokemonEntity, logPos, direction);

                    if (safePos != null) {
                        pokemonEntity.moveTo(Vec3.atLowerCornerOf(safePos));
                    }

                    new SaccharineLogBlockParticlesPacket(logPos, safePos)
                            .sendToPlayersAround(logPos.getX(), logPos.getY(), logPos.getZ(), 64.0, level.dimension(), new Function1<ServerPlayer, Boolean>() {
                                @Override
                                public Boolean invoke(ServerPlayer serverPlayer) {
                                    return false;
                                }
                            });

                    level.playSound(null, logPos, SoundEvents.PLAYER_BURP, SoundSource.NEUTRAL);

                    int radius = TreeHelper.getRadius(level, logPos);
                    if (radius > 0)
                        branch.getFamily().getBranch().ifPresent(b -> b.setRadius(level, logPos, radius, Direction.DOWN));
                }
//                else {
//                    level.setBlock(logPos, Blocks.GOLD_BLOCK.defaultBlockState(), 3);
//                }
            }
            dynamicTrees_Cobblemon_1_21_1$activated = true;
        }
    }

}

//https://gitlab.com/cable-mc/cobblemon/-/blob/main/common/src/main/kotlin/com/cobblemon/mod/common/api/spawning/influence/SaccharineLogSlatheredInfluence.kt