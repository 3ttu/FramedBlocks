package xfacthd.framedblocks.client.model.slope;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.common.FBContent;

public class FramedThreewayCornerGeometry implements Geometry
{
    private final Direction dir;
    private final boolean top;
    private final boolean ySlope;

    public FramedThreewayCornerGeometry(GeometryFactory.Context ctx)
    {
        this.dir = ctx.state().getValue(FramedProperties.FACING_HOR);
        this.top = ctx.state().getValue(FramedProperties.TOP);
        this.ySlope = ctx.state().getValue(FramedProperties.Y_SLOPE);
    }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad)
    {
        Direction quadDir = quad.getDirection();
        if ((quadDir == Direction.UP && top) || (quadDir == Direction.DOWN && !top))
        {
            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutTopBottom(dir.getOpposite(), 1, 0))
                    .export(quadMap.get(quadDir));
        }
        else if (quadDir == dir || quadDir == dir.getCounterClockWise())
        {
            Direction cutDir = quadDir == dir ? dir.getClockWise() : dir.getOpposite();
            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSideLeftRight(cutDir, top ? 1 : 0, top ? 0 : 1))
                    .export(quadMap.get(quadDir));
        }
        else if (quadDir == dir.getOpposite())
        {
            if (!ySlope)
            {
                QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSmallTriangle(dir.getClockWise()))
                    .apply(Modifiers.makeVerticalSlope(!top, 45))
                    .export(quadMap.get(null));
            }

            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSmallTriangle(top ? Direction.DOWN : Direction.UP))
                    .apply(Modifiers.makeHorizontalSlope(false, 45))
                    .export(quadMap.get(null));
        }
        else if (!ySlope && quadDir == dir.getClockWise())
        {
            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSmallTriangle(dir.getOpposite()))
                    .apply(Modifiers.makeVerticalSlope(!top, 45))
                    .export(quadMap.get(null));
        }
        else if (ySlope && ((!top && quadDir == Direction.UP) || (top && quadDir == Direction.DOWN)))
        {
            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSmallTriangle(dir.getOpposite()))
                    .apply(Modifiers.makeVerticalSlope(dir.getClockWise(), 45))
                    .export(quadMap.get(null));

            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSmallTriangle(dir.getClockWise()))
                    .apply(Modifiers.makeVerticalSlope(dir.getOpposite(), 45))
                    .export(quadMap.get(null));
        }
    }



    public static BlockState itemSource()
    {
        return FBContent.BLOCK_FRAMED_THREEWAY_CORNER.get()
                .defaultBlockState()
                .setValue(FramedProperties.FACING_HOR, Direction.SOUTH);
    }
}