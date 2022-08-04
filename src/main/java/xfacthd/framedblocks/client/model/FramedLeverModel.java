package xfacthd.framedblocks.client.model;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;
import xfacthd.framedblocks.api.model.FramedBlockModel;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.util.FramedConstants;
import xfacthd.framedblocks.api.util.Utils;

import java.util.*;

public class FramedLeverModel extends FramedBlockModel
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(FramedConstants.MOD_ID, "block/framed_block");
    private static final float MIN_SMALL = 5F/16F;
    private static final float MAX_SMALL = 11F/16F;
    private static final float MIN_LARGE = 4F/16F;
    private static final float MAX_LARGE = 12F/16F;
    private static final float HEIGHT = 3F/16F;

    private final Direction dir;
    private final AttachFace face;

    public FramedLeverModel(BlockState state, BakedModel baseModel)
    {
        super(state, baseModel);
        dir = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        face = state.getValue(BlockStateProperties.ATTACH_FACE);
    }

    @Override
    protected boolean hasAdditionalQuadsInLayer(RenderType layer)
    {
        return ItemBlockRenderTypes.canRenderInLayer(Blocks.LEVER.defaultBlockState(), layer);
    }

    @Override
    protected void getAdditionalQuads(Map<Direction, List<BakedQuad>> quadMap, BlockState state, Random rand, IModelData data, RenderType layer)
    {
        List<BakedQuad> quads = baseModel.getQuads(state, null, rand, data);
        for (BakedQuad quad : quads)
        {
            if (!quad.getSprite().getName().equals(TEXTURE))
            {
                quadMap.get(null).add(quad);
            }
        }
    }

    @Override
    protected void transformQuad(Map<Direction, List<BakedQuad>> quadMap, BakedQuad quad)
    {
        Direction quadDir = quad.getDirection();
        Direction facing = getFacing();
        boolean quadInDir = quadDir == facing;

        if (Utils.isY(facing))
        {
            if (quadDir.getAxis() == facing.getAxis())
            {
                QuadModifier.geometry(quad)
                        .apply(Modifiers.cutTopBottom(dir.getAxis(), MAX_LARGE))
                        .apply(Modifiers.cutTopBottom(dir.getClockWise().getAxis(), MAX_SMALL))
                        .applyIf(Modifiers.setPosition(HEIGHT), quadInDir)
                        .export(quadMap.get(quadInDir ? null : quadDir));
            }
            else
            {
                boolean smallSide = dir.getAxis() == quadDir.getAxis();

                QuadModifier.geometry(quad)
                        .apply(Modifiers.cutSideUpDown(facing == Direction.DOWN, HEIGHT))
                        .apply(Modifiers.cutSideLeftRight(smallSide ? MAX_SMALL : MAX_LARGE))
                        .apply(Modifiers.setPosition(smallSide ? MAX_LARGE : MAX_SMALL))
                        .export(quadMap.get(null));
            }
        }
        else
        {
            if (quadDir.getAxis() == facing.getAxis())
            {
                QuadModifier.geometry(quad)
                        .apply(Modifiers.cutSide(MIN_SMALL, MIN_LARGE, MAX_SMALL, MAX_LARGE))
                        .applyIf(Modifiers.setPosition(HEIGHT), quadInDir)
                        .export(quadMap.get(quadInDir ? null : quadDir));
            }
            else if (Utils.isY(quadDir))
            {
                QuadModifier.geometry(quad)
                        .apply(Modifiers.cutTopBottom(dir, HEIGHT))
                        .apply(Modifiers.cutTopBottom(dir.getClockWise().getAxis(), MAX_SMALL))
                        .apply(Modifiers.setPosition(MAX_LARGE))
                        .export(quadMap.get(null));
            }
            else
            {
                QuadModifier.geometry(quad)
                        .apply(Modifiers.cutSideLeftRight(dir, HEIGHT))
                        .apply(Modifiers.cutSideUpDown(MAX_LARGE))
                        .apply(Modifiers.setPosition(MAX_SMALL))
                        .export(quadMap.get(null));
            }
        }
    }

    private Direction getFacing()
    {
        return switch (face)
                {
                    case FLOOR -> Direction.UP;
                    case WALL -> dir;
                    case CEILING -> Direction.DOWN;
                };
    }
}