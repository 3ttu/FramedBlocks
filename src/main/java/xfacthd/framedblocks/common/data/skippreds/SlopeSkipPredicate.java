package xfacthd.framedblocks.common.data.skippreds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import xfacthd.framedblocks.common.block.IFramedBlock;
import xfacthd.framedblocks.common.data.*;
import xfacthd.framedblocks.common.util.SideSkipPredicate;

public class SlopeSkipPredicate implements SideSkipPredicate
{
    @Override
    public boolean test(BlockGetter world, BlockPos pos, BlockState state, BlockState adjState, Direction side)
    {
        if (SideSkipPredicate.CTM.test(world, pos, state, adjState, side)) { return true; }
        if (!(adjState.getBlock() instanceof IFramedBlock block)) { return false; }

        BlockType adjBlock = block.getBlockType();
        Direction dir = state.getValue(PropertyHolder.FACING_HOR);
        SlopeType type = state.getValue(PropertyHolder.SLOPE_TYPE);

        if (adjBlock == BlockType.FRAMED_SLOPE)
        {
            return testAgainstSlope(world, pos, dir, type, adjState, side);
        }
        else if (adjBlock == BlockType.FRAMED_DOUBLE_SLOPE)
        {
            return testAgainstDoubleSlope(world, pos, dir, type, adjState, side);
        }
        else if (adjBlock == BlockType.FRAMED_CORNER_SLOPE)
        {
            return testAgainstCorner(world, pos, dir, type, adjState, side);
        }
        else if (adjBlock == BlockType.FRAMED_DOUBLE_CORNER)
        {
            return testAgainstDoubleCorner(world, pos, dir, type, adjState, side);
        }
        else if (adjBlock == BlockType.FRAMED_DOUBLE_PRISM_CORNER || adjBlock == BlockType.FRAMED_DOUBLE_THREEWAY_CORNER)
        {
            return testAgainstDoubleThreewayCorner(world, pos, dir, type, adjState, side);
        }
        else if (adjBlock == BlockType.FRAMED_INNER_CORNER_SLOPE)
        {
            return testAgainstInnerCorner(world, pos, dir, type, adjState, side);
        }
        else if (adjBlock == BlockType.FRAMED_PRISM_CORNER || adjBlock == BlockType.FRAMED_THREEWAY_CORNER)
        {
            return testAgainstThreewayCorner(world, pos, dir, type, adjState, side);
        }
        else if (adjBlock == BlockType.FRAMED_INNER_PRISM_CORNER || adjBlock == BlockType.FRAMED_INNER_THREEWAY_CORNER)
        {
            return testAgainstInnerThreewayCorner(world, pos, dir, type, adjBlock, adjState, side);
        }

        return false;
    }

    private boolean testAgainstSlope(BlockGetter world, BlockPos pos, Direction dir, SlopeType type, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        SlopeType adjType = adjState.getValue(PropertyHolder.SLOPE_TYPE);

        if (type == SlopeType.HORIZONTAL && side.getAxis() == Direction.Axis.Y)
        {
            return dir == adjDir && type == adjType && SideSkipPredicate.compareState(world, pos, side, dir);
        }
        else if (type != SlopeType.HORIZONTAL && (side == dir.getClockWise() || side == dir.getCounterClockWise()))
        {
            return dir == adjDir && type == adjType && SideSkipPredicate.compareState(world, pos, side, dir);
        }
        return false;
    }

    private boolean testAgainstDoubleSlope(BlockGetter world, BlockPos pos, Direction dir, SlopeType type, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        SlopeType adjType = adjState.getValue(PropertyHolder.SLOPE_TYPE);

        if (type == SlopeType.HORIZONTAL && adjType == SlopeType.HORIZONTAL && side.getAxis() == Direction.Axis.Y)
        {
            return (dir == adjDir || adjDir == dir.getOpposite()) && SideSkipPredicate.compareState(world, pos, side, dir);
        }
        else if (type != SlopeType.HORIZONTAL && adjType != SlopeType.HORIZONTAL && (side == dir.getClockWise() || side == dir.getCounterClockWise()))
        {
            return (dir == adjDir && type == adjType) || (dir.getOpposite() == adjDir && type != adjType) && SideSkipPredicate.compareState(world, pos, side, dir);
        }
        return false;
    }

    private boolean testAgainstCorner(BlockGetter world, BlockPos pos, Direction dir, SlopeType type, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        CornerType adjType = adjState.getValue(PropertyHolder.CORNER_TYPE);

        if (side == dir.getClockWise() && adjDir == dir)
        {
            if (type == SlopeType.BOTTOM && (adjType == CornerType.BOTTOM || adjType == CornerType.HORIZONTAL_BOTTOM_LEFT))
            {
                return SideSkipPredicate.compareState(world, pos, side, dir);
            }
            else if (type == SlopeType.TOP && (adjType == CornerType.TOP || adjType == CornerType.HORIZONTAL_TOP_LEFT))
            {
                return SideSkipPredicate.compareState(world, pos, side, dir);
            }
        }
        else if (side == dir.getCounterClockWise())
        {
            if (adjDir == dir)
            {
                if (type == SlopeType.BOTTOM && adjType == CornerType.HORIZONTAL_BOTTOM_RIGHT)
                {
                    return SideSkipPredicate.compareState(world, pos, side, dir);
                }
                else if (type == SlopeType.TOP && adjType == CornerType.HORIZONTAL_TOP_RIGHT)
                {
                    return SideSkipPredicate.compareState(world, pos, side, dir);
                }
            }
            else if (adjDir == dir.getClockWise())
            {
                if (type == SlopeType.BOTTOM && adjType == CornerType.BOTTOM)
                {
                    return SideSkipPredicate.compareState(world, pos, side, dir);
                }
                else if (type == SlopeType.TOP && adjType == CornerType.TOP)
                {
                    return SideSkipPredicate.compareState(world, pos, side, dir);
                }
            }
        }
        else if (side.getAxis() == Direction.Axis.Y && type == SlopeType.HORIZONTAL && ((side == Direction.UP) != (adjType.isTop())))
        {
            if (adjType.isRight())
            {
                return dir == adjDir.getClockWise() && SideSkipPredicate.compareState(world, pos, side, dir);
            }
            else
            {
                return dir == adjDir && SideSkipPredicate.compareState(world, pos, side, dir);
            }
        }
        return false;
    }

    private boolean testAgainstDoubleCorner(BlockGetter world, BlockPos pos, Direction dir, SlopeType type, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        CornerType adjType = adjState.getValue(PropertyHolder.CORNER_TYPE);

        if (adjType.isHorizontal())
        {
            if (type == SlopeType.HORIZONTAL && ((side == Direction.DOWN && !adjType.isTop()) || (side == Direction.UP && adjType.isTop())))
            {
                if ((adjDir == dir || adjDir == dir.getOpposite()) && !adjType.isRight())
                {
                    return SideSkipPredicate.compareState(world, pos, side, dir);
                }
                else if ((adjDir == dir.getClockWise() || adjDir == dir.getCounterClockWise()) && adjType.isRight())
                {
                    return SideSkipPredicate.compareState(world, pos, side, adjDir == dir.getClockWise() ? adjDir.getOpposite() : dir);
                }
            }
            else if (type != SlopeType.HORIZONTAL && adjDir == dir && ((side == dir.getCounterClockWise() && !adjType.isRight()) ||
                                                                       (side == dir.getClockWise() && adjType.isRight()))
            )
            {
                return (type == SlopeType.TOP) == adjType.isTop() && SideSkipPredicate.compareState(world, pos, side, dir);
            }
            else if (type != SlopeType.HORIZONTAL && ((side == dir.getClockWise() && !adjType.isRight()) || (side == dir.getCounterClockWise() && adjType.isRight())))
            {
                return adjDir == dir.getOpposite() && (type == SlopeType.TOP) != adjType.isTop() && SideSkipPredicate.compareState(world, pos, side, dir);
            }
        }
        else
        {
            if ((side == dir.getCounterClockWise() && adjDir == dir) || (side == dir.getClockWise() && adjDir == dir.getClockWise()))
            {
                return (type == SlopeType.TOP) == adjType.isTop() && SideSkipPredicate.compareState(world, pos, side, dir);
            }
            else if ((side == dir.getClockWise() && adjDir == dir.getOpposite()) || (side == dir.getCounterClockWise() && adjDir == dir.getCounterClockWise()))
            {
                Direction face = adjType.isTop() ? Direction.DOWN : Direction.UP;
                return (type == SlopeType.TOP) != adjType.isTop() && SideSkipPredicate.compareState(world, pos, side, face);
            }
        }
        return false;
    }

    private boolean testAgainstDoubleThreewayCorner(BlockGetter world, BlockPos pos, Direction dir, SlopeType type, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        boolean adjTop = adjState.getValue(PropertyHolder.TOP);

        if (type != SlopeType.HORIZONTAL)
        {
            if ((type == SlopeType.TOP) == adjTop && ((side == dir.getCounterClockWise() && adjDir == dir) ||
                                                      (side == dir.getClockWise() && adjDir == dir.getClockWise()))
            )
            {
                return SideSkipPredicate.compareState(world, pos, side, dir);
            }
            else if ((type == SlopeType.TOP) != adjTop && ((side == dir.getCounterClockWise() && adjDir == dir.getCounterClockWise()) ||
                                                           (side == dir.getClockWise() && adjDir == dir.getOpposite()))
            )
            {
                return SideSkipPredicate.compareState(world, pos, side, dir);
            }
        }
        else if ((adjDir == dir || adjDir == dir.getOpposite()) && ((side == Direction.DOWN && !adjTop) || (side == Direction.UP && adjTop)))
        {
            return SideSkipPredicate.compareState(world, pos, side, dir);
        }
        return false;
    }

    private boolean testAgainstInnerCorner(BlockGetter world, BlockPos pos, Direction dir, SlopeType type, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        CornerType adjType = adjState.getValue(PropertyHolder.CORNER_TYPE);

        if (side == dir.getClockWise() && adjDir == dir)
        {
            if (type == SlopeType.BOTTOM && (adjType == CornerType.BOTTOM || adjType == CornerType.HORIZONTAL_BOTTOM_RIGHT))
            {
                return SideSkipPredicate.compareState(world, pos, side, dir);
            }
            else if (type == SlopeType.TOP && (adjType == CornerType.TOP || adjType == CornerType.HORIZONTAL_TOP_RIGHT))
            {
                return SideSkipPredicate.compareState(world, pos, side, dir);
            }
        }
        else if (side == dir.getCounterClockWise())
        {
            if (adjDir == dir)
            {
                if (type == SlopeType.BOTTOM && adjType == CornerType.HORIZONTAL_BOTTOM_LEFT)
                {
                    return SideSkipPredicate.compareState(world, pos, side, dir);
                }
                else if (type == SlopeType.TOP && adjType == CornerType.HORIZONTAL_TOP_LEFT)
                {
                    return SideSkipPredicate.compareState(world, pos, side, dir);
                }
            }
            else if (adjDir == dir.getCounterClockWise())
            {
                if (type == SlopeType.BOTTOM && adjType == CornerType.BOTTOM)
                {
                    return SideSkipPredicate.compareState(world, pos, side, dir);
                }
                else if (type == SlopeType.TOP && adjType == CornerType.TOP)
                {
                    return SideSkipPredicate.compareState(world, pos, side, dir);
                }
            }
        }
        else if (side.getAxis() == Direction.Axis.Y && type == SlopeType.HORIZONTAL && ((side == Direction.UP) == (adjType.isTop())))
        {
            if (adjType.isRight())
            {
                return dir == adjDir.getClockWise() && SideSkipPredicate.compareState(world, pos, side, dir);
            }
            else
            {
                return dir == adjDir && SideSkipPredicate.compareState(world, pos, side, dir);
            }
        }
        return false;
    }

    private boolean testAgainstThreewayCorner(BlockGetter world, BlockPos pos, Direction dir, SlopeType type, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        boolean adjTop = adjState.getValue(PropertyHolder.TOP);

        if (type != SlopeType.HORIZONTAL && adjTop == (type == SlopeType.TOP))
        {
            if (side == dir.getClockWise())
            {
                return dir == adjDir && SideSkipPredicate.compareState(world, pos, side, dir);
            }
            else if (side == dir.getCounterClockWise())
            {
                return adjDir == dir.getClockWise() && SideSkipPredicate.compareState(world, pos, side, dir);
            }
        }
        else if (type == SlopeType.HORIZONTAL && side.getAxis() == Direction.Axis.Y && adjTop == (side == Direction.DOWN))
        {
            return dir == adjDir && SideSkipPredicate.compareState(world, pos, side, dir);
        }
        return false;
    }

    private boolean testAgainstInnerThreewayCorner(BlockGetter world, BlockPos pos, Direction dir, SlopeType type, BlockType adjBlock, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(PropertyHolder.FACING_HOR);
        boolean adjTop = adjState.getValue(PropertyHolder.TOP);

        if (adjBlock == BlockType.FRAMED_INNER_THREEWAY_CORNER) { adjDir = adjDir.getClockWise(); }

        if (type != SlopeType.HORIZONTAL && adjTop == (type == SlopeType.TOP))
        {
            if (side == dir.getClockWise())
            {
                return adjDir == dir.getClockWise() && SideSkipPredicate.compareState(world, pos, side, dir);
            }
            else if (side == dir.getCounterClockWise())
            {
                return dir == adjDir && SideSkipPredicate.compareState(world, pos, side, dir);
            }
        }
        else if (type == SlopeType.HORIZONTAL && side.getAxis() == Direction.Axis.Y && adjTop == (side == Direction.UP))
        {
            return dir == adjDir && SideSkipPredicate.compareState(world, pos, side, dir);
        }
        return false;
    }
}