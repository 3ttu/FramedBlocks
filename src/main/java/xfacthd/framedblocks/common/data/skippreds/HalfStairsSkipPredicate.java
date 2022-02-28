package xfacthd.framedblocks.common.data.skippreds;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.StairsType;
import xfacthd.framedblocks.common.util.SideSkipPredicate;

public class HalfStairsSkipPredicate implements SideSkipPredicate
{
    @Override
    public boolean test(IBlockReader world, BlockPos pos, BlockState state, BlockState adjState, Direction side)
    {
        Direction dir = state.getValue(PropertyHolder.FACING_HOR);
        boolean top = state.getValue(PropertyHolder.TOP);
        boolean right = state.getValue(PropertyHolder.RIGHT);

        Direction stairFace = right ? dir.getClockWise() : dir.getCounterClockWise();
        Direction baseFace = top ? Direction.UP : Direction.DOWN;

        if (adjState.is(FBContent.blockFramedHalfStairs.get()))
        {
            return testAgainstHalfStairs(world, pos, dir, top, right, stairFace, adjState, side);
        }

        if (side == stairFace)
        {
            if (adjState.is(FBContent.blockFramedStairs.get()))
            {
                return testAgainstStairs(world, pos, top, adjState, side);
            }
            else if (adjState.is(FBContent.blockFramedVerticalStairs.get()))
            {
                return testAgainstVerticalStairs(world, pos, dir, top, right, adjState, side);
            }
        }
        else
        {
            if (adjState.is(FBContent.blockFramedSlabEdge.get()))
            {
                return testAgainstSlabEdge(world, pos, dir, top, right, baseFace, adjState, side);
            }
            else if (adjState.is(FBContent.blockFramedCornerPillar.get()))
            {
                return testAgainstCornerPillar(world, pos, dir, right, baseFace, adjState, side);
            }
            else if (adjState.is(FBContent.blockFramedSlabCorner.get()))
            {
                return testAgainstSlabCorner(world, pos, dir, top, right, baseFace, adjState, side);
            }
            else if (adjState.is(FBContent.blockFramedPanel.get()))
            {
                return testAgainstPanel(world, pos, dir, right, baseFace, adjState, side);
            }
            else if (adjState.is(FBContent.blockFramedDoublePanel.get()))
            {
                return testAgainstDoublePanel(world, pos, dir, stairFace, baseFace, adjState, side);
            }
        }

        return false;
    }

    private static boolean testAgainstHalfStairs(IBlockReader world, BlockPos pos, Direction dir, boolean top, boolean right, Direction stairFace, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        boolean adjTop = adjState.getValue(PropertyHolder.TOP);
        boolean adjRight = adjState.getValue(PropertyHolder.RIGHT);

        if (side == stairFace)
        {
            return adjDir == dir && adjTop == top && adjRight != right && SideSkipPredicate.compareState(world, pos, side);
        }
        else if (side.getAxis() == Direction.Axis.Y)
        {
            return adjDir == dir && adjTop != top && adjRight == right && SideSkipPredicate.compareState(world, pos, side);
        }
        else if (side == dir)
        {
            return adjDir == dir.getOpposite() && adjRight != right && SideSkipPredicate.compareState(world, pos, side);
        }

        return false;
    }

    private static boolean testAgainstStairs(IBlockReader world, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(StairsBlock.FACING);
        StairsShape adjShape = adjState.getValue(StairsBlock.SHAPE);
        boolean adjTop = adjState.getValue(StairsBlock.HALF) == Half.TOP;

        return top == adjTop && StairsSkipPredicate.isStairSide(adjShape, adjDir, side.getOpposite()) && SideSkipPredicate.compareState(world, pos, side);
    }

    private static boolean testAgainstVerticalStairs(IBlockReader world, BlockPos pos, Direction dir, boolean top, boolean right, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        StairsType adjType = adjState.getValue(PropertyHolder.STAIRS_TYPE);

        if ((right && adjDir == dir) || (!right && adjDir == dir.getClockWise()))
        {
            return adjType.isTop() != top && SideSkipPredicate.compareState(world, pos, side);
        }

        return false;
    }

    private static boolean testAgainstSlabEdge(IBlockReader world, BlockPos pos, Direction dir, boolean top, boolean right, Direction baseFace, BlockState adjState, Direction side)
    {
        if (side != baseFace && side != dir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        boolean adjTop = adjState.getValue(PropertyHolder.TOP);

        if ((right && adjDir == dir.getClockWise()) || (!right && adjDir == dir.getCounterClockWise()))
        {
            return (adjTop == top) == (side == dir.getOpposite()) && SideSkipPredicate.compareState(world, pos, side);
        }

        return false;
    }

    private static boolean testAgainstCornerPillar(IBlockReader world, BlockPos pos, Direction dir, boolean right, Direction baseFace, BlockState adjState, Direction side)
    {
        if (side != baseFace.getOpposite() && side != dir) { return false; }

        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);

        if (side == dir && ((right && adjDir == dir.getOpposite()) || (!right && adjDir == dir.getCounterClockWise())))
        {
            return SideSkipPredicate.compareState(world, pos, side);
        }
        else if (side == baseFace.getOpposite() && ((right && adjDir == dir.getClockWise()) || (!right && adjDir == dir)))
        {
            return SideSkipPredicate.compareState(world, pos, side);
        }

        return false;
    }

    private static boolean testAgainstSlabCorner(IBlockReader world, BlockPos pos, Direction dir, boolean top, boolean right, Direction baseFace, BlockState adjState, Direction side)
    {
        if (side != baseFace.getOpposite() && side != dir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        boolean adjTop = adjState.getValue(PropertyHolder.TOP);

        if ((right && adjDir == dir.getClockWise()) || (!right && adjDir == dir))
        {
            return adjTop == top && SideSkipPredicate.compareState(world, pos, side);
        }

        return false;
    }

    private static boolean testAgainstPanel(IBlockReader world, BlockPos pos, Direction dir, boolean right, Direction baseFace, BlockState adjState, Direction side)
    {
        if (side != baseFace && side != dir) { return false; }

        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);

        if ((right && adjDir == dir.getClockWise()) || (!right && adjDir == dir.getCounterClockWise()))
        {
            return SideSkipPredicate.compareState(world, pos, side);
        }

        return false;
    }

    private static boolean testAgainstDoublePanel(IBlockReader world, BlockPos pos, Direction dir, Direction stairFace, Direction baseFace, BlockState adjState, Direction side)
    {
        if (side != baseFace && side != dir) { return false; }

        Direction adjDir = adjState.getValue(PropertyHolder.FACING_NE);

        return adjDir.getAxis() != dir.getAxis() && SideSkipPredicate.compareState(world, pos, side, stairFace);
    }
}
