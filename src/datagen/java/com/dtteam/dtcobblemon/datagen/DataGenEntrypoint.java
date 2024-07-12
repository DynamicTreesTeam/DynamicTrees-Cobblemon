package com.dtteam.dtcobblemon.datagen;

import com.dtteam.dtcobblemon.DTCobblemon;
import com.ferreusveritas.dynamictrees.api.GatherDataHelper;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DTCobblemon.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenEntrypoint {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        boolean includeClient = event.includeClient();
//        generator.addProvider(includeClient, new ModLangProvider(packOutput));
//        generator.addProvider(includeClient, new ModBlockStateProvider(packOutput, event.getExistingFileHelper()));
        GatherDataHelper.gatherAllData(DTCobblemon.MODID, event,
                Family.REGISTRY,
                Species.REGISTRY,
                LeavesProperties.REGISTRY,
                Fruit.REGISTRY
        );
    }
}
