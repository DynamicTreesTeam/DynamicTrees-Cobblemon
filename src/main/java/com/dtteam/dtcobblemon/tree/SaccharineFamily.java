package com.dtteam.dtcobblemon.tree;

import com.dtteam.dtcobblemon.DynamicTreesCobblemon;
import com.dtteam.dtcobblemon.branch.SaccharineBranchBlock;
import com.dtteam.dtcobblemon.branch.SlatheredSaccharineBranchBlock;
import com.dtteam.dynamictrees.DynamicTrees;
import com.dtteam.dynamictrees.api.lazyvalue.MutableLazyValue;
import com.dtteam.dynamictrees.api.registry.RegistryHandler;
import com.dtteam.dynamictrees.api.registry.TypedRegistry;
import com.dtteam.dynamictrees.block.branch.BasicBranchBlock;
import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.data.DTDataProvider;
import com.dtteam.dynamictrees.data.builder.BranchLoaderBuilder;
import com.dtteam.dynamictrees.data.generator.BranchStateGenerator;
import com.dtteam.dynamictrees.data.provider.DTBlockStateProvider;
import com.dtteam.dynamictrees.tree.family.Family;
import com.dtteam.dynamictrees.utility.Optionals;
import com.dtteam.dynamictrees.utility.ResourceLocationUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class SaccharineFamily extends Family {
    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(SaccharineFamily::new);

    protected Supplier<BranchBlock> slatheredBranch;
    protected Block primitiveSlatheredLog;
    protected final MutableLazyValue<SlatheredBranchStateGenerator> slatheredBranchStateGenerator;

    public SaccharineFamily(ResourceLocation name) {
        super(name);
        slatheredBranchStateGenerator = MutableLazyValue.supplied(SlatheredBranchStateGenerator::new);
    }

    @Override
    public void setupBlocks() {
        super.setupBlocks();
        this.slatheredBranch = setupBranch(createSlatheredBranch(getBranchName("slathered_")), true);
    }

    @Override
    protected BranchBlock createBranchBlock(ResourceLocation name) {
        BasicBranchBlock branch = new SaccharineBranchBlock(name, this.getProperties());
        if (this.isFireProof()) {
            branch.setFireSpreadSpeed(0).setFlammability(0);
        }

        return branch;
    }

    protected Supplier<BranchBlock> createSlatheredBranch(ResourceLocation name) {
        return RegistryHandler.addBlock(ResourceLocationUtils.suffix(name, this.getBranchNameSuffix()), () -> this.createSlatheredBranchBlock(name));
    }

    protected BranchBlock createSlatheredBranchBlock(ResourceLocation name) {
        BasicBranchBlock branch = new SlatheredSaccharineBranchBlock(name, this.getProperties());
        if (this.isFireProof()) {
            branch.setFireSpreadSpeed(0).setFlammability(0);
        }

        return branch;
    }

    public void setPrimitiveSlatheredLog(Block primitiveLog) {
        this.primitiveSlatheredLog = primitiveLog;
        slatheredBranch.get().setPrimitiveLogDrops(new ItemStack(primitiveLog));
    }

    public Optional<BranchBlock> getSlatheredBranch() {
        return Optionals.ofBlock(slatheredBranch.get());
    }

    public Optional<Block> getPrimitiveSlatheredLog() {
        return Optionals.ofBlock(primitiveSlatheredLog);
    }

    @Override
    public void generateStateData(DTDataProvider.BlockState provider) {
        super.generateStateData(provider);
        this.slatheredBranchStateGenerator.get().generate(provider, this);
    }

    public void addBranchTextures(BiConsumer<String, ResourceLocation> textureConsumer, ResourceLocation primitiveLogLocation, Block sourceBlock) {
        Optional<Block> primSlathered = getPrimitiveSlatheredLog();
        if (primSlathered.isPresent() && primSlathered.get() == sourceBlock){
            ResourceLocation bark = primitiveLogLocation;
            ResourceLocation rings = ResourceLocationUtils.suffix(primitiveLogLocation, "_top");
            if (this.textureOverrides.containsKey("slathered_branch")) {
                bark = this.textureOverrides.get("slathered_branch");
            }

            if (this.textureOverrides.containsKey("branch_top")) {
                rings = this.textureOverrides.get("branch_top");
            }
            textureConsumer.accept("bark", bark);
            textureConsumer.accept("rings", rings);
            return;
        }
        super.addBranchTextures(textureConsumer, primitiveLogLocation, sourceBlock);
    }

    public static class SlatheredBranchStateGenerator extends BranchStateGenerator {

        @Override
        public void generate(DTDataProvider.BlockState prov, Family input, Dependencies dependencies) {
            if (prov instanceof DTBlockStateProvider provider) {
                final BranchBlock branch = dependencies.get(BRANCH);
                final BranchLoaderBuilder builder = provider.models().getBuilder(
                        Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(branch)).getPath()
                ).customLoader(BranchLoaderBuilder.branchBuilders.get(DynamicTreesCobblemon.SLATHERED_BRANCH));
                Block block = dependencies.get(PRIMITIVE_LOG);
                input.addBranchTextures(builder::texture, provider.block(BuiltInRegistries.BLOCK.getKey(block)), block);
                provider.simpleBlock(branch, builder.end());
            }
        }

        public @NotNull Dependencies gatherDependencies(@NotNull Family input) {
            if (input instanceof SaccharineFamily castedInput)
                return (new Dependencies()).append(BRANCH, castedInput.getSlatheredBranch()).append(PRIMITIVE_LOG, castedInput.getPrimitiveSlatheredLog());
            return super.gatherDependencies(input);
        }
    }
}
