package com.dtteam.dtcobblemon.init;

import com.dtteam.dtcobblemon.DynamicTreesCobblemon;
import com.dtteam.dtcobblemon.model.SlatheredBranchBlockModelLoader;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

@EventBusSubscriber(modid = DynamicTreesCobblemon.MOD_ID, value = Dist.CLIENT)
public class DTClientEventHandler {

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelEvent.RegisterGeometryLoaders event) {
        // Register model loaders for baked models.
        event.register(DynamicTreesCobblemon.SLATHERED_BRANCH, new SlatheredBranchBlockModelLoader());
    }

}
