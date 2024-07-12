package com.dtteam.dtcobblemon.fruit;

import com.ferreusveritas.dynamictrees.api.data.Generator;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.data.provider.DTBlockStateProvider;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.util.MutableLazyValue;
import net.minecraft.resources.ResourceLocation;

public class ApricornFruit extends Fruit {
    public static final TypedRegistry.EntryType<Fruit> TYPE =TypedRegistry.newType(ApricornFruit::new);;
    protected final MutableLazyValue<Generator<DTBlockStateProvider, Fruit>> fruitGenerator =
            MutableLazyValue.supplied(ApricornFruitStateGenerator::new);

    public ApricornFruit(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public void generateStateData(DTBlockStateProvider provider) {
        super.generateStateData(provider);
        this.fruitGenerator.get().generate(provider, this, fruitGenerator.get().gatherDependencies(this));
    }
}
