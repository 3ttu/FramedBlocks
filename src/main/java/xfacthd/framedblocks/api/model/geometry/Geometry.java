package xfacthd.framedblocks.api.model.geometry;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.model.cache.QuadCacheKey;
import xfacthd.framedblocks.api.model.cache.SimpleQuadCacheKey;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.predicate.fullface.FullFacePredicate;

import java.util.ArrayList;
import java.util.List;

public interface Geometry
{
    /**
     * Called for each {@link BakedQuad} of the camo block's model for whose side this block's
     * {@link FullFacePredicate#test(BlockState, Direction)} returns {@code false}.
     * @param quadMap The target map to put all final quads into
     * @param quad The source quad. Must not be modified directly, use {@link QuadModifier}s to
     *             modify the quad
     * @param data The {@link ModelData}
     */
    default void transformQuad(QuadMap quadMap, BakedQuad quad, ModelData data)
    {
        transformQuad(quadMap, quad);
    }

    /**
     * Called for each {@link BakedQuad} of the camo block's model for whose side this block's
     * {@link FullFacePredicate#test(BlockState, Direction)} returns {@code false}.
     * @param quadMap The target map to put all final quads into
     * @param quad The source quad. Must not be modified directly, use {@link QuadModifier}s to
     *             modify the quad
     */
    void transformQuad(QuadMap quadMap, BakedQuad quad);

    /**
     * Post-process quads on faces that return {@code false} from {@link FullFacePredicate#test(BlockState, Direction)}<br>
     * Any additional processing done in this method should be as fast as possible
     */
    default List<BakedQuad> postProcessUncachedQuads(List<BakedQuad> quads)
    {
        return quads;
    }

    /**
     * Return true if the base model loaded from JSON should be used when no camo is applied without going
     * through the quad manipulation process
     */
    default boolean forceUngeneratedBaseModel()
    {
        return false;
    }

    /**
     * Return true if the base model loaded from JSON should be used instead of the Framed Cube model
     * when no camo is applied. Quad manipulation will still be done if
     * {@link Geometry#forceUngeneratedBaseModel()} returns false
     * @apiNote Must return true if {@link Geometry#forceUngeneratedBaseModel()} returns true
     */
    default boolean useBaseModel()
    {
        return forceUngeneratedBaseModel();
    }

    /**
     * Return true if all quads should be submitted for transformation, even if their cull-face would be filtered
     * by the {@link FullFacePredicate}
     */
    default boolean transformAllQuads()
    {
        return false;
    }

    /**
     * Return true if the full set of {@link RenderType}s including overlay render types returned by
     * {@link Geometry#getAdditionalRenderTypes(RandomSource, ModelData)} are only dependent on the
     * {@link BlockState} associated with this model and/or the camo BlockState in the model data and can
     * therefore be cached based on the camo BlockState
     */
    default boolean canFullyCacheRenderTypes()
    {
        return true;
    }

    /**
     * Return {@link RenderType}s which contain additional quads (i.e. overlays) or {@link ChunkRenderTypeSet#none()}
     * when no additional render types are present
     */
    default ChunkRenderTypeSet getAdditionalRenderTypes(RandomSource rand, ModelData extraData)
    {
        return ChunkRenderTypeSet.none();
    }

    /**
     * Add additional quads to faces that return {@code true} from {@link FullFacePredicate#test(BlockState, Direction)}<br>
     * The result of this method will NOT be cached, execution should therefore be as fast as possible
     */
    default void getAdditionalQuads(
            ArrayList<BakedQuad> quads,
            Direction side,
            BlockState state,
            RandomSource rand,
            ModelData data,
            RenderType renderType
    )
    { }

    /**
     * Add additional quads to faces that return {@code false} from {@link FullFacePredicate#test(BlockState, Direction)}<br>
     * The result of this method will be cached, processing time is therefore not critical
     */
    default void getAdditionalQuads(
            QuadMap quadMap,
            BlockState state,
            RandomSource rand,
            ModelData data,
            RenderType renderType
    )
    { }

    /**
     * Return a custom {@link QuadCacheKey} that holds additional metadata which influences the resulting quads.
     * @implNote The resulting object must at least store the given {@link BlockState} and connected textures context object
     * and should either be a record or have an otherwise properly implemented {@code hashCode()} and {@code equals()}
     * implementation
     * @param state The {@link BlockState} of the camo applied to the block
     * @param ctCtx The current connected textures context object, may be null
     * @param data The {@link ModelData} from the {@link FramedBlockEntity}
     */
    default QuadCacheKey makeCacheKey(BlockState state, Object ctCtx, ModelData data)
    {
        return new SimpleQuadCacheKey(state, ctCtx);
    }

    /**
     * Apply transformations to the item model when it is rendered in hand
     */
    default void applyInHandTransformation(PoseStack poseStack, ItemDisplayContext ctx) { }
}
