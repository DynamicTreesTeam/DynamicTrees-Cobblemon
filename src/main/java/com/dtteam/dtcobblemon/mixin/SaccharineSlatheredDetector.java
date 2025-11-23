package com.dtteam.dtcobblemon.mixin;

import com.cobblemon.mod.common.CobblemonPoiTypes;
import com.cobblemon.mod.common.api.spawning.influence.SaccharineLogSlatheredInfluence;
import com.cobblemon.mod.common.api.spawning.influence.SpatialSpawningZoneInfluence;
import com.cobblemon.mod.common.api.spawning.influence.SpawningZoneInfluence;
import com.cobblemon.mod.common.api.spawning.prospecting.SaccharineLogSlatheredDetector;
import com.cobblemon.mod.common.api.spawning.spawner.Spawner;
import com.cobblemon.mod.common.api.spawning.spawner.SpawningZoneInput;
import com.dtteam.dtcobblemon.init.DTCobblemonPoiTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SaccharineLogSlatheredDetector.class)
public class SaccharineSlatheredDetector {

    @Final @Shadow
    public static int RANGE;

    @Inject(method = "detectFromInput", at = @At("RETURN"), cancellable = true, remap = false)
    private void detectFromInput(Spawner spawner, SpawningZoneInput input, CallbackInfoReturnable<List<SpawningZoneInfluence>> cir){
        List<SpawningZoneInfluence> listOfInfluences = cir.getReturnValue();

        ServerLevel world = input.getWorld();

        int searchRange = RANGE + (int) Math.ceil(Math.sqrt(Math.pow(input.getLength(), 2) + Math.pow(input.getWidth(), 2)));

        BlockPos centerPos = BlockPos.containing(input.getCenter());

        List<BlockPos> honeyLogPositions = world.getPoiManager()
                .findAll(holder -> holder.is(DTCobblemonPoiTypes.SACCHARINE_BRANCH_SLATHERED.getKey()),
                        pos -> true,
                        centerPos,
                        searchRange,
                        PoiManager.Occupancy.ANY
                ).toList();

        for (BlockPos pos : honeyLogPositions) {
            listOfInfluences.add(new SpatialSpawningZoneInfluence(pos, (float) RANGE, new SaccharineLogSlatheredInfluence(pos)));
        }

        cir.setReturnValue(listOfInfluences);
    }

}

//https://gitlab.com/cable-mc/cobblemon/-/blob/main/common/src/main/kotlin/com/cobblemon/mod/common/api/spawning/prospecting/SaccharineLogSlatheredDetector.kt