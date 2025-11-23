package com.dtteam.dtcobblemon.model;

import com.dtteam.dynamictrees.model.geometry.BranchBlockModelGeometry;
import com.dtteam.dynamictrees.model.loader.SurfaceRootBlockModelLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.ResourceLocationException;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;
import java.util.List;

/**
 * Loads a branch block model from a Json file, with useful warnings when things aren't found.
 *
 * <p>Can also be used by sub-classes to load other models, like for roots in
 * {@link SurfaceRootBlockModelLoader}.</p>
 *
 * @author Harley O'Connor
 */
public class SlatheredBranchBlockModelLoader implements IGeometryLoader<SlatheredBranchBlockModelGeometry> {

    public static final Logger LOGGER = LogManager.getLogger();

    private static final String TEXTURES = "textures";
    private static final String BARK = "bark";
    private static final String SLATHERED = "slathered_textures";
    private static final String RINGS = "rings";

    @Override
    public SlatheredBranchBlockModelGeometry read(JsonObject modelObject, JsonDeserializationContext deserializationContext) throws JsonParseException {
        final JsonObject textures = this.getTexturesObject(modelObject);

        return this.getModelGeometry(this.getBarkTextureLocation(textures), this.getRingsTextureLocation(textures), this.getSlatheredTexturesLocation(modelObject));
    }

    protected JsonObject getTexturesObject(final JsonObject modelContents) {
        if (!modelContents.has(TEXTURES) || !modelContents.get(TEXTURES).isJsonObject()) {
            this.throwRequiresElement(TEXTURES, "Json Object");
        }

        return modelContents.getAsJsonObject(TEXTURES);
    }

    protected List<ResourceLocation> getSlatheredTexturesLocation(final JsonObject object) {
        return this.getTextureLocations(object, SLATHERED);
    }

    protected ResourceLocation getBarkTextureLocation(final JsonObject textureObject) {
        return this.getTextureLocation(textureObject, BARK);
    }

    protected ResourceLocation getRingsTextureLocation(final JsonObject textureObject) {
        return this.getTextureLocation(textureObject, RINGS);
    }

    protected ResourceLocation getTextureLocation(final JsonObject textureObject, final String textureElement) {
        try {
            return this.getLocationOrThrow(this.getOrThrow(textureObject, textureElement));
        } catch (final RuntimeException e) {
            LOGGER.error("{} missing or did not have valid \"{}\" texture location element, using missing " +
                    "texture.", this.getModelTypeName(), textureElement);
            return MissingTextureAtlasSprite.getLocation();
        }
    }

    protected List<ResourceLocation> getTextureLocations(final JsonObject textureObject, final String textureElement) {
        try {
            return this.getLocationsOrThrow(this.getArrayOrThrow(textureObject, textureElement));
        } catch (final RuntimeException e) {
            LOGGER.error("{} missing or did not have valid \"{}\" texture location list element, using missing " +
                    "texture.", this.getModelTypeName(), textureElement);
            return List.of(MissingTextureAtlasSprite.getLocation());
        }
    }

    protected String getOrThrow(final JsonObject jsonObject, final String identifier) {
        if (jsonObject.get(identifier) == null || !jsonObject.get(identifier).isJsonPrimitive() ||
                !jsonObject.get(identifier).getAsJsonPrimitive().isString()) {
            this.throwRequiresElement(identifier, "String");
        }

        return jsonObject.get(identifier).getAsString();
    }

    protected void throwRequiresElement(final String element, final String expectedType) {
        throw new RuntimeException(this.getModelTypeName() + " requires a valid \"" + element + "\" element of " +
                "type " + expectedType + ".");
    }

    protected JsonArray getArrayOrThrow(final JsonObject jsonObject, final String identifier) {
        if (jsonObject.get(identifier) != null && jsonObject.get(identifier).isJsonPrimitive() && jsonObject.getAsJsonPrimitive(identifier).isString()){
            JsonArray pseudoArray = new JsonArray();
            pseudoArray.add(getOrThrow(jsonObject, identifier));
            return pseudoArray;
        }
        if (jsonObject.get(identifier) == null || !jsonObject.get(identifier).isJsonArray()) {
            this.throwRequiresElement(identifier, "Array");
        }
        JsonArray array = jsonObject.get(identifier).getAsJsonArray();
        array.forEach(jsonElement -> {
            if (!jsonElement.isJsonPrimitive() || !jsonElement.getAsJsonPrimitive().isString()){
                throw new RuntimeException(this.getModelTypeName() + " requires all elements of \"" + identifier + "\" to be of type String.");
            }
        });
        return array;
    }

    protected ResourceLocation getLocationOrThrow(final String location) {
        try {
            return ResourceLocation.parse(location);
        } catch (ResourceLocationException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<ResourceLocation> getLocationsOrThrow(final JsonArray array) {
        try {
            List<ResourceLocation> locations = new LinkedList<>();
            array.forEach(element -> locations.add(ResourceLocation.parse(element.getAsString())));
            return locations;
        } catch (ResourceLocationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @return The type of model the class is loading. Useful for warnings when using sub-classes.
     */
    protected String getModelTypeName() {
        return "Slathered Branch";
    }

    /**
     * Gets the {@link BranchBlockModelGeometry} object from the given bark and rings texture locations.
     * Can be overridden by subclasses to provide their custom {@link BranchBlockModelGeometry}.
     *
     * @param barkTextureLocation The {@link ResourceLocation} object for the bark.
     * @param ringsTextureLocation The {@link ResourceLocation} object for the rings.
     * @return The {@link BranchBlockModelGeometry} object.
     */
    protected SlatheredBranchBlockModelGeometry getModelGeometry(final ResourceLocation barkTextureLocation, final ResourceLocation ringsTextureLocation, final List<ResourceLocation> slatheredTextureLocations) {
        return new SlatheredBranchBlockModelGeometry(barkTextureLocation, ringsTextureLocation, slatheredTextureLocations);
    }

}