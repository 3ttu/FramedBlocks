package xfacthd.framedblocks.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.FramedBlockModel;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.util.FramedProperties;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.PropertyHolder;

import java.util.List;
import java.util.Map;

public class FramedFlatSlopeSlabCornerModel extends FramedBlockModel
{
    private final Direction facing;
    private final boolean top;
    private final boolean topHalf;

    public FramedFlatSlopeSlabCornerModel(BlockState state, BakedModel baseModel)
    {
        super(state, baseModel);
        this.facing = state.getValue(FramedProperties.FACING_HOR);
        this.top = state.getValue(FramedProperties.TOP);
        this.topHalf = state.getValue(PropertyHolder.TOP_HALF);
    }

    @Override
    protected void transformQuad(Map<Direction, List<BakedQuad>> quadMap, BakedQuad quad)
    {
        Direction face = quad.getDirection();
        boolean offset = top != topHalf;

        if (face == facing.getOpposite() || face == facing.getClockWise())
        {
            boolean right = face == facing.getClockWise();
            float lenTop = top ? 1F : 0F;
            float lenBot = top ? 0F : 1F;

            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSideLeftRight(right, lenTop, lenBot))
                    .apply(Modifiers.makeVerticalSlope(!top, FramedSlopeSlabModel.SLOPE_ANGLE))
                    .applyIf(Modifiers.offset(top ? Direction.DOWN : Direction.UP, .5F), offset)
                    .export(quadMap.get(null));
        }
        else if (face == facing || face == facing.getCounterClockWise())
        {
            boolean rightFace = face == facing;
            float right = rightFace ? (offset ? .5F : 0) : (offset ? 1 : .5F);
            float left =  rightFace ? (offset ? 1 : .5F) : (offset ? .5F : 0);

            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSideUpDown(top, right, left))
                    .applyIf(Modifiers.cutSideUpDown(!top, .5F), offset)
                    .export(quadMap.get(face));
        }
        else if ((top && !topHalf && face == Direction.UP) || (!top && topHalf && face == Direction.DOWN))
        {
            QuadModifier.geometry(quad)
                    .apply(Modifiers.setPosition(.5F))
                    .export(quadMap.get(null));
        }
    }

    @Override
    protected void applyInHandTransformation(PoseStack poseStack, ItemTransforms.TransformType type)
    {
        poseStack.translate(0, .5, 0);
    }



    public static BlockState itemSource()
    {
        return FBContent.blockFramedFlatSlopeSlabCorner.get()
                .defaultBlockState()
                .setValue(FramedProperties.FACING_HOR, Direction.SOUTH);
    }
}