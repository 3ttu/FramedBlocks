package xfacthd.framedblocks.common.data.skippreds.slab;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.util.*;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.SlopeType;
import xfacthd.framedblocks.common.data.skippreds.stairs.SlopedStairsSkipPredicate;
import xfacthd.framedblocks.common.data.skippreds.slope.VerticalHalfSlopeSkipPredicate;
import xfacthd.framedblocks.common.data.skippreds.slopeslab.FlatElevatedSlopeSlabCornerSkipPredicate;
import xfacthd.framedblocks.common.data.skippreds.slopeslab.FlatInnerSlopeSlabCornerSkipPredicate;
import xfacthd.framedblocks.common.data.skippreds.stairs.StairsSkipPredicate;

public final class SlabSkipPredicate implements SideSkipPredicate
{
    @Override
    public boolean test(BlockGetter level, BlockPos pos, BlockState state, BlockState adjState, Direction side)
    {
        if (Utils.isY(side)) { return SideSkipPredicate.CTM.test(level, pos, state, adjState, side); }

        if (adjState.getBlock() instanceof IFramedBlock block && block.getBlockType() instanceof BlockType type)
        {
            boolean top = state.getValue(FramedProperties.TOP);

            return switch (type)
            {
                case FRAMED_SLAB -> testAgainstSlab(level, pos, top, adjState, side);
                case FRAMED_DOUBLE_SLAB -> testAgainstDoubleSlab(level, pos, top, side);
                case FRAMED_SLAB_EDGE -> testAgainstEdge(level, pos, top, adjState, side);
                case FRAMED_STAIRS -> testAgainstStairs(level, pos, top, adjState, side);
                case FRAMED_DOUBLE_STAIRS -> testAgainstDoubleStairs(level, pos, top, adjState, side);
                case FRAMED_SLOPE_SLAB -> testAgainstSlopeSlab(level, pos, top, adjState, side);
                case FRAMED_ELEVATED_SLOPE_SLAB -> testAgainstElevatedSlopeSlab(level, pos, top, adjState, side);
                case FRAMED_DOUBLE_SLOPE_SLAB -> testAgainstDoubleSlopeSlab(level, pos, top, adjState, side);
                case FRAMED_INV_DOUBLE_SLOPE_SLAB -> testAgainstInverseDoubleSlopeSlab(level, pos, top, adjState, side);
                case FRAMED_ELEVATED_DOUBLE_SLOPE_SLAB -> testAgainstElevatedDoubleSlopeSlab(level, pos, top, adjState, side);
                case FRAMED_FLAT_INNER_SLOPE_SLAB_CORNER -> testAgainstFlatInnerSlopeSlabCorner(level, pos, top, adjState, side);
                case FRAMED_FLAT_ELEV_SLOPE_SLAB_CORNER -> testAgainstFlatElevatedSlopeSlabCorner(level, pos, top, adjState, side);
                case FRAMED_FLAT_DOUBLE_SLOPE_SLAB_CORNER -> testAgainstFlatDoubleSlopeSlabCorner(level, pos, top, adjState, side);
                case FRAMED_FLAT_INV_DOUBLE_SLOPE_SLAB_CORNER -> testAgainstFlatInverseDoubleSlopeSlabCorner(level, pos, top, adjState, side);
                case FRAMED_FLAT_ELEV_DOUBLE_SLOPE_SLAB_CORNER -> testAgainstFlatElevatedDoubleSlopeSlabCorner(level, pos, top, adjState, side);
                case FRAMED_VERTICAL_HALF_STAIRS, FRAMED_VERTICAL_HALF_SLOPE -> testAgainstVerticalHalfStairs(level, pos, top, adjState, side);
                case FRAMED_DIVIDED_SLOPE -> testAgainstDividedSlope(level, pos, top, adjState, side);
                case FRAMED_VERTICAL_DOUBLE_HALF_SLOPE -> testAgainstVerticalDoubleHalfSlope(level, pos, top, adjState, side);
                case FRAMED_SLOPED_STAIRS -> testAgainstSlopedStairs(level, pos, top, adjState, side);
                default -> false;
            };
        }

        return false;
    }

    private static boolean testAgainstSlab(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        if (top != adjState.getValue(FramedProperties.TOP)) { return false; }

        Direction camoSide = top ? Direction.UP : Direction.DOWN;
        return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
    }

    private static boolean testAgainstDoubleSlab(BlockGetter level, BlockPos pos, boolean top, Direction side)
    {
        Direction face = top ? Direction.UP : Direction.DOWN;
        return SideSkipPredicate.compareState(level, pos, side, face, face);
    }

    private static boolean testAgainstEdge(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        if (top != adjState.getValue(FramedProperties.TOP)) { return false; }
        if (adjState.getValue(FramedProperties.FACING_HOR) != side.getOpposite()) { return false; }

        Direction camoSide = top ? Direction.UP : Direction.DOWN;
        return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
    }

    private static boolean testAgainstStairs(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(BlockStateProperties.HORIZONTAL_FACING);
        StairsShape adjShape = adjState.getValue(BlockStateProperties.STAIRS_SHAPE);
        boolean adjTop = adjState.getValue(BlockStateProperties.HALF) == Half.TOP;

        if (top == adjTop && StairsSkipPredicate.isSlabSide(adjShape, adjDir, side.getOpposite()))
        {
            return SideSkipPredicate.compareState(level, pos, side);
        }
        return false;
    }

    private static boolean testAgainstDoubleStairs(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(BlockStateProperties.HORIZONTAL_FACING);

        Direction face = top ? Direction.UP : Direction.DOWN;
        return adjDir == side && SideSkipPredicate.compareState(level, pos, side, face, face);
    }

    private static boolean testAgainstSlopeSlab(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTopHalf = adjState.getValue(PropertyHolder.TOP_HALF);

        if (Utils.isY(side)) { return false; }

        Direction camoSide = top ? Direction.UP : Direction.DOWN;
        return adjDir == side.getOpposite() && adjTopHalf == top && SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
    }

    private static boolean testAgainstElevatedSlopeSlab(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (Utils.isY(side)) { return false; }

        Direction camoSide = top ? Direction.UP : Direction.DOWN;
        return adjDir == side && adjTop == top && SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
    }

    private static boolean testAgainstDoubleSlopeSlab(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTopHalf = adjState.getValue(PropertyHolder.TOP_HALF);

        if (Utils.isY(side)) { return false; }

        Direction camoSide = top ? Direction.UP : Direction.DOWN;
        return (adjDir == side || adjDir == side.getOpposite()) && adjTopHalf == top && SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
    }

    private static boolean testAgainstInverseDoubleSlopeSlab(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);

        if (Utils.isY(side)) { return false; }

        Direction camoSide = top ? Direction.UP : Direction.DOWN;
        return ((adjDir == side && !top) || (adjDir == side.getOpposite() && top)) && SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
    }

    private static boolean testAgainstElevatedDoubleSlopeSlab(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);

        if (!Utils.isY(side) && adjDir == side)
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }

        return false;
    }

    private static boolean testAgainstFlatInnerSlopeSlabCorner(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTopHalf = adjState.getValue(PropertyHolder.TOP_HALF);

        if (adjTopHalf == top && FlatInnerSlopeSlabCornerSkipPredicate.isSlabSide(adjDir, side.getOpposite()))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, adjDir);
        }

        return false;
    }

    private static boolean testAgainstFlatElevatedSlopeSlabCorner(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop == top && FlatElevatedSlopeSlabCornerSkipPredicate.isSlabSide(adjDir, side.getOpposite()))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }

        return false;
    }

    private static boolean testAgainstFlatDoubleSlopeSlabCorner(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTopHalf = adjState.getValue(PropertyHolder.TOP_HALF);

        if (adjTopHalf == top && FlatInnerSlopeSlabCornerSkipPredicate.isSlabSide(adjDir, side.getOpposite()))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, side.getOpposite());
        }

        return false;
    }

    private static boolean testAgainstFlatInverseDoubleSlopeSlabCorner(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop == top && FlatInnerSlopeSlabCornerSkipPredicate.isInverseSlabSide(adjDir, side.getOpposite()))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }

        return false;
    }

    private static boolean testAgainstFlatElevatedDoubleSlopeSlabCorner(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);

        if (FlatInnerSlopeSlabCornerSkipPredicate.isInverseSlabSide(adjDir, side.getOpposite()))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }

        return false;
    }

    private static boolean testAgainstVerticalHalfStairs(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        if (Utils.isY(side)) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop == top && (adjDir == side.getOpposite() || adjDir == side.getCounterClockWise()))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }

        return false;
    }

    private static boolean testAgainstDividedSlope(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        if (Utils.isY(side)) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        SlopeType adjType = adjState.getValue(PropertyHolder.SLOPE_TYPE);

        if (adjType == SlopeType.HORIZONTAL && VerticalHalfSlopeSkipPredicate.isSlabFace(adjDir, side.getOpposite()))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, camoSide);
        }

        return false;
    }

    private static boolean testAgainstVerticalDoubleHalfSlope(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        Direction camoSide = top ? Direction.UP : Direction.DOWN;
        return adjTop == top && SideSkipPredicate.compareState(level, pos, side, camoSide, side.getOpposite());
    }

    private static boolean testAgainstSlopedStairs(BlockGetter level, BlockPos pos, boolean top, BlockState adjState, Direction side)
    {
        if (Utils.isY(side)) { return false; }

        Direction adjDir = adjState.getValue(FramedProperties.FACING_HOR);
        boolean adjTop = adjState.getValue(FramedProperties.TOP);

        if (adjTop == top && SlopedStairsSkipPredicate.isSlabFace(adjDir, side.getOpposite()))
        {
            Direction camoSide = top ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(level, pos, side, camoSide, side.getOpposite());
        }

        return false;
    }
}