package com.dtteam.dtcobblemon.fruit;

import com.dtteam.dynamictrees.api.lazyvalue.MutableLazyValue;
import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.fruit.Fruit;
import com.dtteam.dynamictrees.data.DTDataProvider;
import com.dtteam.dynamictrees.data.Generator;
import com.dtteam.dynamictrees.data.provider.DTBlockStateProvider;
import net.minecraft.resources.ResourceLocation;

public class ApricornFruit extends Fruit {
    public static final TypedRegistry.EntryType<Fruit> TYPE =TypedRegistry.newType(ApricornFruit::new);
    protected final MutableLazyValue<Generator<DTBlockStateProvider, Fruit>> fruitGenerator =
            MutableLazyValue.supplied(ApricornFruitStateGenerator::new);

    public ApricornFruit(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public void generateStateData(DTDataProvider.BlockState prov) {
        if (prov instanceof DTBlockStateProvider provider){
            super.generateStateData(provider);
            this.fruitGenerator.get().generate(provider, this, fruitGenerator.get().gatherDependencies(this));
        }
    }
}
