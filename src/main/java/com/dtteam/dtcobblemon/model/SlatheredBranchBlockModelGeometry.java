package com.dtteam.dtcobblemon.model;

import com.dtteam.dynamictrees.model.baked.BasicBranchBlockBakedModel;
import com.dtteam.dynamictrees.model.baked.ThickBranchBlockBakedModel;
import com.dtteam.dynamictrees.model.geometry.SurfaceRootBlockModelGeometry;
import com.dtteam.dynamictrees.model.loader.BranchBlockModelLoader;
import com.dtteam.dynamictrees.tree.family.Family;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

/**
 * Bakes {@link BasicBranchBlockBakedModel} from bark and rings texture locations given by {@link
 * BranchBlockModelLoader}.
 *
 * <p>Can also be used by sub-classes to bake other models, like for roots in
 * {@link SurfaceRootBlockModelGeometry}.</p>
 *
 * @author Harley O'Connor
 */
@OnlyIn(Dist.CLIENT)
public class SlatheredBranchBlockModelGeometry implements IUnbakedGeometry<SlatheredBranchBlockModelGeometry> {
    protected final Set<ResourceLocation> textures = new HashSet<>();
    protected final ResourceLocation barkTextureLocation;
    protected final List<ResourceLocation> slatheredTextureLocations;
    protected final ResourceLocation ringsTextureLocation;

    public SlatheredBranchBlockModelGeometry(@Nullable final ResourceLocation barkTextureLocation, @Nullable final ResourceLocation ringsTextureLocation, @Nullable final List<ResourceLocation> slatheredTextureLocations) {
        this.barkTextureLocation = barkTextureLocation;
        this.ringsTextureLocation = ringsTextureLocation;
        this.slatheredTextureLocations = slatheredTextureLocations;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides itemOverrides) {
        return new SlatheredBranchBlockBakedModel(context, this.barkTextureLocation, this.ringsTextureLocation, this.slatheredTextureLocations, spriteGetter);
    }

}