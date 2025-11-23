package com.dtteam.dtcobblemon.mixin;

import com.cobblemon.mod.common.client.net.effect.SaccharineLogBlockParticlesHandler;
import com.cobblemon.mod.common.net.messages.client.effect.SaccharineLogBlockParticlesPacket;
import com.dtteam.dtcobblemon.branch.SlatheredSaccharineBranchBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SaccharineLogBlockParticlesHandler.class)
public class SaccharineParticlesHandler {
    @Inject(method = "handle(Lcom/cobblemon/mod/common/net/messages/client/effect/SaccharineLogBlockParticlesPacket;Lnet/minecraft/client/Minecraft;)V", at = @At("HEAD"), remap = false)
    private void handlePacket(SaccharineLogBlockParticlesPacket packet, Minecraft client, CallbackInfo ci){
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockState blockState = level.getBlockState(packet.getBlockPos());
        Block block = blockState.getBlock();
        if (block instanceof SlatheredSaccharineBranchBlock branch) {
            Direction direction = blockState.getValue(HorizontalDirectionalBlock.FACING);
            branch.spawnParticlesAtBlockFace(
                    new BlockParticleOption(ParticleTypes.BLOCK, Blocks.HONEY_BLOCK.defaultBlockState()), level, packet.getBlockPos(), direction, 15);
            branch.spawnParticlesAtBlockFace(
                    ParticleTypes.FALLING_HONEY, level, packet.getBlockPos(), direction, 15);
        }

    }
}

//https://gitlab.com/cable-mc/cobblemon/-/blob/main/common/src/main/kotlin/com/cobblemon/mod/common/client/net/effect/SaccharineLogBlockParticlesHandler.kt