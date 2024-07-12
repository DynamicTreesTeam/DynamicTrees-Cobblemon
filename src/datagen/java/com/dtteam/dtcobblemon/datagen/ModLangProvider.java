package com.dtteam.dtcobblemon.datagen;

import com.dtteam.dtcobblemon.DTCobblemon;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.google.common.collect.ImmutableMap;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ModLangProvider extends LanguageProvider {
    protected static final Map<String, String> REPLACE_LIST = ImmutableMap.of(
            "tnt", "TNT",
            "sus", "",
            "branch", "Tree"
    );

    public ModLangProvider(PackOutput gen) {
        super(gen, DTCobblemon.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        ForgeRegistries.ITEMS.getValues().stream().filter(item -> ForgeRegistries.ITEMS.getKey(item).getNamespace().equals(DTCobblemon.MODID)).forEach(this::itemLang);
        ForgeRegistries.BLOCKS.getValues().stream().filter(item -> ForgeRegistries.BLOCKS.getKey(item).getNamespace().equals(DTCobblemon.MODID)).forEach(this::blockLang);
        Species.REGISTRY.forEach(
                species -> {
                    if(species.getRegistryName().getNamespace().equals(DTCobblemon.MODID)) {
                        speciesLang(species);
                    }
                }
        );
    }

    protected void itemLang(Item entry) {
        if (!(entry instanceof BlockItem) || entry instanceof ItemNameBlockItem) {
            addItem(() -> entry, checkReplace(ForgeRegistries.ITEMS.getKey(entry)));
        }
    }

    protected void speciesLang(Species entry) {
        add(entry.getLocalizedName(), checkReplace(entry.getRegistryName()));
    }

    protected void blockLang(Block entry) {
        addBlock(() -> entry, checkReplace(ForgeRegistries.BLOCKS.getKey(entry)));
    }

    protected void entityLang(RegistryObject<EntityType<?>> entry) {
        addEntityType(entry, checkReplace(ForgeRegistries.ENTITY_TYPES.getKey(entry.get())));
    }

    protected String checkReplace(ResourceLocation registryObject) {
        return Arrays.stream(registryObject.getPath().split("_"))
                .map(this::checkReplace)
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "))
                .trim();
    }

    protected String checkReplace(String string) {
        return REPLACE_LIST.containsKey(string) ? REPLACE_LIST.get(string) : StringUtils.capitalize(string);
    }
}
