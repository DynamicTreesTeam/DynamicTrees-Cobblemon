package com.dtteam.dtcobblemon.init;

import com.dtteam.dtcobblemon.DynamicTreesCobblemon;
import com.dtteam.dtcobblemon.tree.SaccharineFamily;
import com.dtteam.dynamictrees.tree.family.Family;
import com.google.common.collect.ImmutableSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;

public class DTCobblemonPoiTypes {

    public static final DeferredRegister<PoiType> POIs_REGISTRY =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, DynamicTreesCobblemon.MOD_ID);

    public static DeferredHolder<PoiType, PoiType> SACCHARINE_BRANCH_SLATHERED;

    public static void register(IEventBus bus) {
        Family.REGISTRY.runOnNextLock(Family.REGISTRY.generateIfValidRunnable(DynamicTreesCobblemon.location("saccharine"), family -> {
            if (family.isValid() && family instanceof SaccharineFamily saccharine){
                SACCHARINE_BRANCH_SLATHERED = POIs_REGISTRY.register("saccharine_branch_slathered", () ->
                        new PoiType(ImmutableSet.copyOf(saccharine.getSlatheredBranch().get().getStateDefinition().getPossibleStates()), 0, 1)
                );
                POIs_REGISTRY.register(bus);
            }
        }, () -> LogManager.getLogger().warn("Could not register saccharine branch block POI.")));
    }

}
