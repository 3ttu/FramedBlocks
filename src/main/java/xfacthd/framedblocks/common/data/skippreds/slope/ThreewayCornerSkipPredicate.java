package xfacthd.framedblocks.common.data.skippreds.slope;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.util.*;
import xfacthd.framedblocks.common.data.*;
import xfacthd.framedblocks.common.data.property.*;
import xfacthd.framedblocks.common.data.skippreds.stairs.VerticalSlopedStairsSkipPredicate;
import xfacthd.framedblocks.common.util.FramedUtils;

public final class ThreewayCornerSkipPredicate implements SideSkipPredicate
{
    @Override
    public boolean test(BlockGetter level, BlockPos pos, BlockState state, BlockState adjState, Direction side)
    {
        if (SideSkipPredicate.CTM.test(level, pos, state, adjState, side)) { return true; }

        if (adjState.getBlock() instanceof IFramedBlock block && block.getBlockType() instanceof BlockType type)
        {
            Direction dir = state.getValue(FramedProperties.FACING_HOR);
            boolean top = state.getValue(FramedProperties.TOP);

            return switch (type)
            {
                case FRAMED_PRISM_CORNER, FRAMED_THREEWAY_CORNER -> testAgainstThreewayCorner(level, pos, dir, top, adjState, side);
                case FRAMED_INNER_PRISM_CORNER, FRAMED_INNER_THREEWAY_CORNER -> testAgainstInnerThreewayCorner(level, pos, dir, top, adjState, side);
                case FRAMED_DOUBLE_PRISM_CORNER, FRAMED_DOUBLE_THREEWAY_CORNER -> testAgainstDoubleThreewayCorner(level, pos, dir, top, adjState, side);
                case FRAMED_SLOPE,
                     FRAMED_RAIL_SLOPE,
                     FRAMED_POWERED_RAIL_SLOPE,
                     FRAMED_DETECTOR_RAIL_SLOPE,
                     FRAMED_ACTIVATOR_RAIL_SLOPE,
                     FRAMED_FANCY_RAIL_SLOPE,
                     FRAMED_FANCY_POWERED_RAIL_SLOPE,
                     FRAMED_FANCY_DETECTOR_RAIL_SLOPE,
                     FRAMED_FANCY_ACTIVATOR_RAIL_SLOPE,
                     FRAMED_DIVIDED_SLOPE -> testAgainstSlope(level, pos, dir, top, adjState, side);
                case FRAMED_DOUBLE_SLOPE -> testAgainstDoubleSlope(level, pos, dir, top, adjState, side);
                case FRAMED_CORNER_SLOPE -> testAgainstCorner(level, pos, dir, top, adjState, side);
                case FRAMED_INNER_CORNER_SLOPE -> testAgainstInnerCorner(level, pos, dir, top, adjState, side);
                case FRAMED_DOUBLE_CORNER -> testAgainstDoubleCorner(level, pos, dir, top, adjState, side);
                case FRAMED_HALF_SLOPE -> testAgainstHalfSlope(level, pos, dir, top, adjState, side);
                case FRAMED_DOUBLE_HALF_SLOPE -> testAgainstDoubleHalfSlope(level, pos, dir, top, adjState, side);
                case FRAMED_VERTICAL_HALF_SLOPE -> testAgainstVerticalHalfSlope(level, pos, dir, top, adjState, side);
                case FRAMED_VERTICAL_DOUBLE_HALF_SLOPE -> testAgainstVerticalDoubleHalfSlope(level, pos, dir, top, adjState, side);
                case FRAMED_SLOPED_STAIRS -> testAgainstSlopedStairs(level, pos, dir, top, adjState, side);
                case FRAMED_VERTICAL_SLOPED_STAIRS -> testAgainstVerticalSlopedStairs(level, pos, dir, top, adjState, side);
                default -> false;
            };
        }

        return false;
    }

    private static boolean testAgainstThreewayCorner(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (Utils.isY(side) && adjTop != top && adjDir == dir && (side == Direction.UP) == top)
        {
            return SideSkipPredicate.compareState(level, pos, side, side, side);
        }
        else if (adjTop == top && ((side == dir && adjDir == dir.getCounterClockWise()) || (side == dir.getCounterClockWise() && adjDir == dir.getClockWise())))
        {
            return SideSkipPredicate.compareState(level, pos, side, side, side);
        }
        return false;
    }

    private static boolean testAgainstInnerThreewayCorner(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop == top && adjDir == dir && (side == dir || side == dir.getCounterClockWise() || (side == Direction.DOWN && !top) || (side == Direction.UP && top)))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }
        return false;
    }

    private static boolean testAgainstDoubleThreewayCorner(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop == top && adjDir == dir && (side == dir || side == dir.getCounterClockWise() || (side == Direction.UP && top) || (side == Direction.DOWN && !top)))
        {
            return SideSkipPredicate.compareState(level, pos, side, dir, dir);
        }
        else if (adjTop == top && adjDir == dir.getOpposite() && ((side == Direction.UP && top) || (side == Direction.DOWN || !top)))
        {
            return SideSkipPredicate.compareState(level, pos, side, dir, dir);
        }
        else if (adjTop != top && ((side == dir.getCounterClockWise() && adjDir == dir.getCounterClockWise()) || (side == dir && adjDir == dir.getClockWise())))
        {
            return SideSkipPredicate.compareState(level, pos, side, adjDir.getOpposite(), adjDir.getOpposite());
        }
        return false;
    }

    private static boolean testAgainstSlope(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = FramedUtils.getSlopeBlockFacing(adjState);
        SlopeType adjType = FramedUtils.getSlopeType(adjState);

        if ((side == dir && adjDir == dir.getCounterClockWise()) || (side == dir.getCounterClockWise() && adjDir == dir))
        {
            boolean adjTop = adjType == SlopeType.TOP;
            return adjType != SlopeType.HORIZONTAL && adjTop == top && SideSkipPredicate.compareState(level, pos, side);
        }
        else if ((!top && side == Direction.DOWN) || (top && side == Direction.UP))
        {
            return adjType == SlopeType.HORIZONTAL && adjDir == dir && SideSkipPredicate.compareState(level, pos, side);
        }
        return false;
    }

    private static boolean testAgainstDoubleSlope(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        SlopeType adjType = adjState.getValue(PropertyHolder.SLOPE_TYPE);

        if (adjType != SlopeType.HORIZONTAL)
        {
            if ((side == dir.getCounterClockWise() && adjDir == dir) || (side == dir && adjDir == dir.getCounterClockWise()))
            {
                return (adjType == SlopeType.TOP) == top && SideSkipPredicate.compareState(level, pos, side, adjDir.getOpposite(), adjDir.getOpposite());
            }
            else if ((side == dir.getCounterClockWise() && adjDir == dir.getOpposite()) || (side == dir && adjDir == dir.getClockWise()))
            {
                return (adjType == SlopeType.TOP) != top && SideSkipPredicate.compareState(level, pos, side, adjDir.getOpposite(), adjDir.getOpposite());
            }
        }
        else if ((!top && side == Direction.DOWN || top && side == Direction.UP) && (adjDir == dir || adjDir == dir.getOpposite()))
        {
            return SideSkipPredicate.compareState(level, pos, side, dir, dir);
        }
        return false;
    }

    private static boolean testAgainstCorner(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        CornerType adjType = adjState.getValue(PropertyHolder.CORNER_TYPE);

        if (((side == dir && adjDir == dir.getCounterClockWise()) || (side == dir.getCounterClockWise() && adjDir == dir.getClockWise())) && !adjType.isHorizontal())
        {
            return adjType.isTop() == top && SideSkipPredicate.compareState(level, pos, side, side, side);
        }
        else if ((side == dir && adjDir == dir.getCounterClockWise() && !adjType.isRight()) ||
                 (side == dir.getCounterClockWise() && adjDir == dir && adjType.isRight())
        )
        {
            return adjType.isTop() == top && adjType.isHorizontal() && SideSkipPredicate.compareState(level, pos, side, side, side);
        }
        else if (((!top && side == Direction.DOWN) || (top && side == Direction.UP)) &&
                ((adjDir == dir.getCounterClockWise() && adjType.isRight()) || (adjDir == dir && !adjType.isRight()))
        )
        {
            return adjType.isTop() != top && adjType.isHorizontal() && SideSkipPredicate.compareState(level, pos, side, side, side);
        }
        return false;
    }

    private static boolean testAgainstInnerCorner(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        CornerType adjType = adjState.getValue(PropertyHolder.CORNER_TYPE);

        if (!adjType.isHorizontal() && adjDir == dir && (side == dir || side == dir.getCounterClockWise()) && adjType.isTop() == top)
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }
        else if (adjType.isHorizontal() && ((side == dir && adjType.isRight()) || (side == dir.getCounterClockWise() && !adjType.isRight())) && adjType.isTop() == top)
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }
        else if (adjType.isHorizontal() && ((side == Direction.DOWN && !top) || (side == Direction.UP && top)) && adjType.isTop() == top)
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return ((!adjType.isRight() && adjDir == dir) || (adjType.isRight() && adjDir == dir.getCounterClockWise())) &&
                    SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }
        return false;
    }

    private static boolean testAgainstDoubleCorner(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        CornerType adjType = adjState.getValue(PropertyHolder.CORNER_TYPE);

        if (!adjType.isHorizontal())
        {
            if (adjDir == dir && adjType.isTop() == top && (side == dir || side == dir.getCounterClockWise()))
            {
                Direction camoSide = top ? Direction.UP : Direction.DOWN;
                return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
            }
            else if (adjType.isTop() != top && ((side == dir && adjDir == dir.getClockWise()) ||
                                                (side == dir.getCounterClockWise() && adjDir == dir.getCounterClockWise()))
            )
            {
                Direction camoSide = top ? Direction.UP : Direction.DOWN;
                return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
            }
        }
        else if (adjType.isTop() == top)
        {
            if ((!adjType.isRight() && adjDir == dir) || (adjType.isRight() && adjDir == dir.getCounterClockWise()))
            {
                if ((side == Direction.DOWN && !top) || (side == Direction.UP && top))
                {
                    return SideSkipPredicate.compareState(level, pos, side, adjDir, adjDir);
                }
                else if ((side == dir && adjType.isRight()) || (side == dir.getCounterClockWise() && !adjType.isRight()))
                {
                    return SideSkipPredicate.compareState(level, pos, side, adjDir, adjDir);
                }
            }
            else if (Utils.isY(side) && ((!adjType.isRight() && adjDir == dir.getOpposite()) || (adjType.isRight() && adjDir == dir.getClockWise()))
            )
            {
                if ((side == Direction.DOWN && !top) || (side == Direction.UP && top))
                {
                    return SideSkipPredicate.compareState(level, pos, side, adjDir.getOpposite(), adjDir.getOpposite());
                }
            }
        }
        else if ((side == dir && adjDir == dir.getClockWise() && !adjType.isRight()) ||
                 (side == dir.getCounterClockWise() && adjDir == dir.getOpposite() && adjType.isRight())
        )
        {
            return SideSkipPredicate.compareState(level, pos, side, adjDir.getOpposite(), adjDir.getOpposite());
        }
        return false;
    }

    private static boolean testAgainstHalfSlope(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);
        boolean adjRight = adjState.getValue(PropertyHolder.RIGHT);

        if ((!adjRight && side == dir && adjDir == dir.getCounterClockWise()) || (adjRight && side == dir.getCounterClockWise() && adjDir == dir))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return adjTop == top && SideSkipPredicate.compareState(level, pos, side, camoSide, side.getOpposite());
        }
        return false;
    }

    private static boolean testAgainstDoubleHalfSlope(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjRight = adjState.getValue(PropertyHolder.RIGHT);

        if (!top && ((!adjRight && side == dir && adjDir == dir.getCounterClockWise()) || (adjRight && side == dir.getCounterClockWise() && adjDir == dir)))
        {
            return SideSkipPredicate.compareState(level, pos, side, Direction.DOWN, Direction.DOWN);
        }
        else if (top && ((!adjRight && side == dir.getCounterClockWise() && adjDir == dir.getOpposite()) || (adjRight && side == dir && adjDir == dir.getClockWise())))
        {
            return SideSkipPredicate.compareState(level, pos, side, Direction.UP, Direction.UP);
        }
        return false;
    }

    private static boolean testAgainstVerticalHalfSlope(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        if ((top && side != Direction.UP) || (!top && side != Direction.DOWN)) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop != top && adjDir == dir)
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, side.getOpposite());
        }

        return false;
    }

    private static boolean testAgainstVerticalDoubleHalfSlope(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        if ((top && side != Direction.UP) || (!top && side != Direction.DOWN)) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop != top && adjDir.getAxis() == dir.getAxis())
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, dir);
        }

        return false;
    }

    private static boolean testAgainstSlopedStairs(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        if ((top && side != Direction.UP) || (!top && side != Direction.DOWN)) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop == top && adjDir == dir)
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, dir);
        }

        return false;
    }

    private static boolean testAgainstVerticalSlopedStairs(BlockGetter level, BlockPos pos, Direction dir, boolean top, BlockState adjState, Direction side)
    {
        if (Utils.isY(side) || (side != dir && side != dir.getCounterClockWise())) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        HorizontalRotation adjRot = adjState.getValue(PropertyHolder.ROTATION);

        boolean vert = VerticalSlopedStairsSkipPredicate.isPanelFace(adjDir, adjRot, top ? Direction.DOWN : Direction.UP);
        Direction horDir = adjDir == dir ? dir.getClockWise() : dir.getOpposite();
        boolean hor = VerticalSlopedStairsSkipPredicate.isPanelFace(adjDir, adjRot, horDir);

        Direction camoSide = top ? Direction.UP : Direction.DOWN;
        return vert && hor && SideSkipPredicate.compareState(level, pos, side, camoSide, dir);
    }
}