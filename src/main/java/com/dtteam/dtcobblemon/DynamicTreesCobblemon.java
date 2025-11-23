package com.dtteam.dtcobblemon;

import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.block.leaves.LeavesProperties;
import com.dtteam.dynamictrees.data.GatherDataHelper;
import com.dtteam.dynamictrees.registry.NeoForgeRegistryHandler;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.tree.species.Species;
import com.dtteam.dynamictrees.treepack.Resources;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(DynamicTreesCobblemon.MOD_ID)
public class DynamicTreesCobblemon {

    public static final String MOD_ID = "dtcobblemon";
    public static boolean DisableCobblemonTrees = true;

    public DynamicTreesCobblemon(IEventBus bus, ModContainer modContainer) {
        bus.addListener(this::gatherData);

        NeoForgeRegistryHandler.setup(MOD_ID, bus);
    }

    private void gatherData(final GatherDataEvent event) {
        Resources.MANAGER.gatherData();
        GatherDataHelper.gatherAllData(
                MOD_ID, event,
                Family.REGISTRY,
                Species.REGISTRY,
                LeavesProperties.REGISTRY,
                Fruit.REGISTRY
        );
    }

    public static ResourceLocation Location (String path){
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

}
