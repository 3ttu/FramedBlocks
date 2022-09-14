package xfacthd.framedblocks.common.data.skippreds;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.util.FramedProperties;
import xfacthd.framedblocks.api.util.SideSkipPredicate;
import xfacthd.framedblocks.common.data.*;
import xfacthd.framedblocks.common.data.property.HorizontalRotation;
import xfacthd.framedblocks.common.data.property.StairsType;

public final class SlopePanelSkipPredicate implements SideSkipPredicate
{
    @Override
    public boolean test(BlockGetter level, BlockPos pos, BlockState state, BlockState adjState, Direction side)
    {
        Direction dir = state.getValue(FramedProperties.FACING_HOR);
        HorizontalRotation rot = state.getValue(PropertyHolder.ROTATION);
        Direction rotDir = rot.withFacing(dir);
        boolean front = state.getValue(PropertyHolder.FRONT);

        if (side == dir)
        {
            return !front && SideSkipPredicate.CTM.test(level, pos, state, adjState, side);
        }

        if (adjState.getBlock() instanceof IFramedBlock block && block.getBlockType() instanceof BlockType type)
        {
            return switch (type)
            {
                case FRAMED_SLOPE_PANEL -> testAgainstSlopePanel(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_EXTENDED_SLOPE_PANEL -> testAgainstExtendedSlopePanel(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_DOUBLE_SLOPE_PANEL -> testAgainstDoubleSlopePanel(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_INV_DOUBLE_SLOPE_PANEL -> testAgainstInverseDoubleSlopePanel(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_EXTENDED_DOUBLE_SLOPE_PANEL -> testAgainstExtendedDoubleSlopePanel(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_SLAB_EDGE -> testAgainstSlabEdge(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_PANEL -> testAgainstPanel(level, pos, dir, rotDir, front, adjState, side);
                case FRAMED_DOUBLE_PANEL -> testAgainstDoublePanel(level, pos, dir, rotDir, front, adjState, side);
                case FRAMED_CORNER_PILLAR -> testAgainstCornerPillar(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_STAIRS -> testAgainstStairs(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_DOUBLE_STAIRS -> testAgainstDoubleStairs(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_VERTICAL_STAIRS -> testAgainstVerticalStairs(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_VERTICAL_DOUBLE_STAIRS -> testAgainstVerticalDoubleStairs(level, pos, dir, rot, rotDir, front, adjState, side);
                case FRAMED_HALF_STAIRS -> testAgainstHalfStairs(level, pos, dir, rot, rotDir, front, adjState, side);
                default -> false;
            };
        }

        return false;
    }

    private static boolean testAgainstSlopePanel(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        HorizontalRotation adjRot = adjState.getValue(PropertyHolder.ROTATION);
        boolean adjFront = adjState.getValue(PropertyHolder.FRONT);

        if (side == rotDir.getOpposite())
        {
            if (adjRot == rot && adjDir == dir.getOpposite() && adjFront != front)
            {
                return SideSkipPredicate.compareState(level, pos, side);
            }
            else if (adjRot == rot.getOpposite() && adjDir == dir && adjFront == front)
            {
                return SideSkipPredicate.compareState(level, pos, side);
            }
        }
        else if (side.getAxis() != rotDir.getAxis() && side.getAxis() != dir.getAxis())
        {
            Direction camoDir = rotDir.getOpposite();
            return adjDir == dir && adjFront == front && adjRot == rot && SideSkipPredicate.compareState(level, pos, side, camoDir, camoDir);
        }

        return false;
    }

    private static boolean testAgainstExtendedSlopePanel(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (side != rotDir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        HorizontalRotation adjRot = adjState.getValue(PropertyHolder.ROTATION);

        if (adjRot == rot && ((adjDir == dir && !front) || (adjDir == dir.getOpposite() && front)))
        {
            return SideSkipPredicate.compareState(level, pos, side);
        }

        return false;
    }

    private static boolean testAgainstDoubleSlopePanel(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        HorizontalRotation adjRot = adjState.getValue(PropertyHolder.ROTATION);
        boolean adjFront = adjState.getValue(PropertyHolder.FRONT);

        if (side == rotDir.getOpposite() && (adjRot == rot || adjRot == rot.getOpposite()) && ((adjDir == dir && adjFront == front) || (adjDir == dir.getOpposite() && adjFront != front)))
        {
            return SideSkipPredicate.compareState(level, pos, side);
        }
        else if (side.getAxis() != dir.getAxis() && side.getAxis() != rotDir.getAxis() && ((adjFront == front && adjRot == rot) || (adjFront != front && adjRot == rot.getOpposite())))
        {
            if ((adjDir == dir && adjFront == front) || (adjDir == dir.getOpposite() && adjFront != front))
            {
                return SideSkipPredicate.compareState(level, pos, side, rotDir.getOpposite(), rotDir.getOpposite());
            }
        }

        return false;
    }

    private static boolean testAgainstInverseDoubleSlopePanel(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (side != rotDir.getOpposite() && (side.getAxis() == dir.getAxis() || (side.getAxis() == rotDir.getAxis()))) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        HorizontalRotation adjRot = adjState.getValue(PropertyHolder.ROTATION);

        if (side == rotDir.getOpposite())
        {
            if (adjDir.getAxis() == dir.getAxis() && ((adjRot == rot && !front) || (adjRot == rot.getOpposite() && front)))
            {
                return SideSkipPredicate.compareState(level, pos, side);
            }
        }
        else if (front && side.getAxis() != dir.getAxis() && side.getAxis() != rotDir.getAxis())
        {
            if (adjDir.getAxis() == dir.getAxis() && adjRot == rot)
            {
                Direction camoDir = rotDir.getOpposite();
                return SideSkipPredicate.compareState(level, pos, side, camoDir, camoDir);
            }
        }

        return false;
    }

    private static boolean testAgainstExtendedDoubleSlopePanel(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        if (adjDir.getAxis() != dir.getAxis()) { return false; }

        HorizontalRotation adjRot = adjState.getValue(PropertyHolder.ROTATION);

        if (side == rotDir.getOpposite())
        {
            Direction camoDir = front ? dir.getOpposite() : dir;
            return (adjRot == rot || adjRot == rot.getOpposite()) && SideSkipPredicate.compareState(level, pos, side, side, camoDir);
        }
        else if (!front && side.getAxis() != rotDir.getAxis() && side.getAxis() != dir.getAxis())
        {
            Direction camoDir = rotDir.getOpposite();
            return adjDir == dir.getOpposite() && adjRot == rot && SideSkipPredicate.compareState(level, pos, side, camoDir, camoDir);
        }

        return false;
    }

    private static boolean testAgainstSlabEdge(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (!rot.isVertical() || side != rotDir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop == (rot == HorizontalRotation.UP) && ((adjDir == dir && !front) || (adjDir == dir.getOpposite() && front)))
        {
            return SideSkipPredicate.compareState(level, pos, side);
        }

        return false;
    }

    private static boolean testAgainstPanel(BlockGetter level, BlockPos pos, Direction dir, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (side != rotDir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);

        if ((adjDir == dir && !front) || (adjDir == dir.getOpposite() && front))
        {
            return SideSkipPredicate.compareState(level, pos, side);
        }

        return false;
    }

    private static boolean testAgainstDoublePanel(BlockGetter level, BlockPos pos, Direction dir, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (side != rotDir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_NE);

        return (adjDir == dir || adjDir == dir.getOpposite()) && SideSkipPredicate.compareState(level, pos, side, side, front ? dir.getOpposite() : dir);
    }

    private static boolean testAgainstCornerPillar(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (rot.isVertical() || side != rotDir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);

        if ((!front && (adjDir == dir || adjDir == dir.getClockWise())) || (front && (adjDir == dir.getOpposite() || adjDir == dir.getCounterClockWise())))
        {
            return SideSkipPredicate.compareState(level, pos, side);
        }

        return false;
    }

    private static boolean testAgainstStairs(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (!rot.isVertical() || side != rotDir.getOpposite()) { return false; }
        if (adjState.getValue(StairBlock.SHAPE) != StairsShape.STRAIGHT) { return false; }

        Direction adjDir = adjState.getValue(StairBlock.FACING);
        boolean adjTop = adjState.getValue(StairBlock.HALF) == Half.TOP;

        if ((!front && adjDir == dir) || (front && adjDir == dir.getOpposite()))
        {
            return adjTop == (rot == HorizontalRotation.DOWN) && SideSkipPredicate.compareState(level, pos, side);
        }

        return false;
    }

    private static boolean testAgainstDoubleStairs(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (!rot.isVertical() || side != rotDir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(StairBlock.FACING);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if ((adjDir == dir || adjDir == dir.getOpposite()) && adjTop == (rot == HorizontalRotation.DOWN))
        {
            return SideSkipPredicate.compareState(level, pos, side, side, front ? dir.getOpposite() : dir);
        }

        return false;
    }

    private static boolean testAgainstVerticalStairs(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (rot.isVertical() || side != rotDir.getOpposite()) { return false; }
        if (adjState.getValue(PropertyHolder.STAIRS_TYPE) != StairsType.VERTICAL) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);

        if (!front && ((adjDir == dir && rot == HorizontalRotation.RIGHT) || (adjDir == dir.getClockWise() && rot == HorizontalRotation.LEFT)))
        {
            return SideSkipPredicate.compareState(level, pos, side);
        }
        else if (front && ((adjDir == dir.getOpposite() && rot == HorizontalRotation.LEFT) || (adjDir == dir.getCounterClockWise() && rot == HorizontalRotation.RIGHT)))
        {
            return SideSkipPredicate.compareState(level, pos, side);
        }

        return false;
    }

    private static boolean testAgainstVerticalDoubleStairs(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (rot.isVertical() || side != rotDir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);

        if (!front && ((adjDir == dir && rot == HorizontalRotation.RIGHT) || (adjDir == dir.getClockWise() && rot == HorizontalRotation.LEFT)))
        {
            return SideSkipPredicate.compareState(level, pos, side, side, adjDir);
        }
        else if (front && ((adjDir == dir.getOpposite() && rot == HorizontalRotation.LEFT) || (adjDir == dir.getCounterClockWise() && rot == HorizontalRotation.RIGHT)))
        {
            return SideSkipPredicate.compareState(level, pos, side, side, adjDir);
        }
        else if ((front && (adjDir == dir || adjDir == dir.getClockWise())) || (!front && (adjDir == dir.getOpposite() || adjDir == dir.getCounterClockWise())))
        {
            return SideSkipPredicate.compareState(level, pos, side, side, adjDir.getOpposite());
        }

        return false;
    }

    private static boolean testAgainstHalfStairs(BlockGetter level, BlockPos pos, Direction dir, HorizontalRotation rot, Direction rotDir, boolean front, BlockState adjState, Direction side)
    {
        if (side != rotDir.getOpposite()) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjRight = adjState.getValue(PropertyHolder.RIGHT);

        if (rot.isVertical())
        {
            if (adjState.getValue(FramedProperties.TOP) != (rot == HorizontalRotation.UP)) { return false; }

            if (!adjRight && ((adjDir == dir.getClockWise() && !front) || (adjDir == dir.getCounterClockWise() && front)))
            {
                return SideSkipPredicate.compareState(level, pos, side);
            }
            else if (adjRight && ((adjDir == dir.getCounterClockWise() && !front) || (adjDir == dir.getClockWise() && front)))
            {
                return SideSkipPredicate.compareState(level, pos, side);
            }
        }
        else if (adjDir == side.getOpposite())
        {
            if (!adjRight && ((adjDir == dir.getClockWise() && !front) || (adjDir == dir.getCounterClockWise() && front)))
            {
                return SideSkipPredicate.compareState(level, pos, side);
            }
            else if (adjRight && ((adjDir == dir.getCounterClockWise() && !front) || (adjDir == dir.getClockWise() && front)))
            {
                return SideSkipPredicate.compareState(level, pos, side);
            }
        }

        return false;
    }
}
