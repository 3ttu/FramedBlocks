package xfacthd.framedblocks.common.block.stairs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.block.IFramedBlock;
import xfacthd.framedblocks.api.shapes.ShapeProvider;
import xfacthd.framedblocks.api.shapes.ShapeUtils;
import xfacthd.framedblocks.api.util.*;
import xfacthd.framedblocks.common.block.FramedBlock;
import xfacthd.framedblocks.common.block.slope.FramedHalfSlopeBlock;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.HorizontalRotation;

public class FramedVerticalSlopedStairsBlock extends FramedBlock
{
    public FramedVerticalSlopedStairsBlock()
    {
        super(BlockType.FRAMED_VERTICAL_SLOPED_STAIRS);
        registerDefaultState(defaultBlockState().setValue(FramedProperties.Y_SLOPE, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(
                FramedProperties.FACING_HOR, PropertyHolder.ROTATION, FramedProperties.SOLID,
                BlockStateProperties.WATERLOGGED, FramedProperties.Y_SLOPE
        );
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        BlockState state = defaultBlockState();

        Direction facing = context.getHorizontalDirection();
        state = state.setValue(FramedProperties.FACING_HOR, facing);

        Direction face = context.getClickedFace();
        HorizontalRotation rot;
        if (face == facing.getOpposite())
        {
            rot = HorizontalRotation.fromWallCorner(context.getClickLocation(), face);
        }
        else
        {
            rot = HorizontalRotation.fromPerpendicularWallCorner(facing, face, context.getClickLocation());
        }
        state = state.setValue(PropertyHolder.ROTATION, rot);

        return withWater(state, context.getLevel(), context.getClickedPos());
    }

    @Override
    public boolean handleBlockLeftClick(BlockState state, Level level, BlockPos pos, Player player)
    {
        return IFramedBlock.toggleYSlope(state, level, pos, player);
    }

    @Override
    public BlockState rotate(BlockState state, BlockHitResult hit, Rotation rot)
    {
        Direction facing = state.getValue(FramedProperties.FACING_HOR);
        Direction face = hit.getDirection();

        HorizontalRotation horRot = state.getValue(PropertyHolder.ROTATION);
        Direction rotDir = horRot.withFacing(facing);
        Direction rotDirTwo = horRot.rotate(Rotation.COUNTERCLOCKWISE_90).withFacing(facing);

        if (!Utils.isY(face) && (face == rotDir || face == rotDirTwo))
        {
            double frac = Utils.fractionInDir(hit.getLocation(), facing.getOpposite());
            if (frac >= .5)
            {
                face = Direction.UP;
            }
        }
        return rotate(state, face, rot);
    }

    @Override
    public BlockState rotate(BlockState state, Direction face, Rotation rot)
    {
        Direction facing = state.getValue(FramedProperties.FACING_HOR);
        if (Utils.isY(face))
        {
            return state.setValue(FramedProperties.FACING_HOR, rot.rotate(facing));
        }
        else if (face.getAxis() == facing.getAxis())
        {
            HorizontalRotation horRot = state.getValue(PropertyHolder.ROTATION);
            return state.setValue(PropertyHolder.ROTATION, horRot.rotate(rot));
        }
        return state;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return rotate(state, Direction.UP, rotation);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        if (mirror == Mirror.NONE) { return state; }

        Direction dir = state.getValue(FramedProperties.FACING_HOR);
        if ((mirror == Mirror.FRONT_BACK && Utils.isX(dir)) || (mirror == Mirror.LEFT_RIGHT && Utils.isZ(dir)))
        {
            state = state.setValue(FramedProperties.FACING_HOR, dir.getOpposite());
        }

        HorizontalRotation horRot = state.getValue(PropertyHolder.ROTATION);
        horRot = horRot.isVertical() ? horRot.rotate(Rotation.CLOCKWISE_90) : horRot.rotate(Rotation.COUNTERCLOCKWISE_90);
        state = state.setValue(PropertyHolder.ROTATION, horRot);

        return state;
    }



    public static ShapeProvider generateShapes(ImmutableList<BlockState> states)
    {
        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        VoxelShape panelShape = box(0, 0, 0, 16, 16, 8);

        VoxelShape shapeUp =  ShapeUtils.orUnoptimized(
                panelShape,
                ShapeUtils.rotateShapeUnoptimized(
                        Direction.NORTH,
                        Direction.EAST,
                        FramedHalfSlopeBlock.SHAPES.get(new FramedHalfSlopeBlock.ShapeKey(false, true))
                )
        );

        VoxelShape shapeDown = ShapeUtils.orUnoptimized(
                panelShape,
                ShapeUtils.rotateShapeUnoptimized(
                        Direction.NORTH,
                        Direction.WEST,
                        FramedHalfSlopeBlock.SHAPES.get(new FramedHalfSlopeBlock.ShapeKey(true, false))
                )
        );

        VoxelShape shapeRight =  ShapeUtils.orUnoptimized(
                panelShape,
                ShapeUtils.rotateShapeUnoptimized(
                        Direction.NORTH,
                        Direction.WEST,
                        FramedHalfSlopeBlock.SHAPES.get(new FramedHalfSlopeBlock.ShapeKey(false, false))
                )
        );

        VoxelShape shapeLeft =  ShapeUtils.orUnoptimized(
                panelShape,
                ShapeUtils.rotateShapeUnoptimized(
                        Direction.NORTH,
                        Direction.EAST,
                        FramedHalfSlopeBlock.SHAPES.get(new FramedHalfSlopeBlock.ShapeKey(true, true))
                )
        );

        VoxelShape[] shapes = new VoxelShape[4 * 4];
        for (Direction dir : Direction.Plane.HORIZONTAL)
        {
            int horId = dir.get2DDataValue();
            shapes[horId] = ShapeUtils.rotateShape(
                    Direction.NORTH, dir, shapeUp
            );
            shapes[horId | (HorizontalRotation.DOWN.ordinal() << 2)] = ShapeUtils.rotateShape(
                    Direction.NORTH, dir, shapeDown
            );
            shapes[horId | (HorizontalRotation.RIGHT.ordinal() << 2)] = ShapeUtils.rotateShape(
                    Direction.NORTH, dir, shapeRight
            );
            shapes[horId | (HorizontalRotation.LEFT.ordinal() << 2)] = ShapeUtils.rotateShape(
                    Direction.NORTH, dir, shapeLeft
            );
        }

        for (BlockState state : states)
        {
            Direction dir = state.getValue(FramedProperties.FACING_HOR);
            HorizontalRotation rot = state.getValue(PropertyHolder.ROTATION);
            int idx = dir.get2DDataValue() | (rot.ordinal() << 2);
            builder.put(state, shapes[idx]);
        }

        return ShapeProvider.of(builder.build());
    }
}
