package xfacthd.framedblocks.common.block.slopepanel;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import xfacthd.framedblocks.api.util.*;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.block.AbstractFramedDoubleBlock;
import xfacthd.framedblocks.common.blockentity.FramedFlatExtendedDoubleSlopePanelCornerBlockEntity;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.data.property.HorizontalRotation;

public class FramedFlatExtendedDoubleSlopePanelCornerBlock extends AbstractFramedDoubleBlock
{
    public static final CtmPredicate CTM_PREDICATE = CtmPredicate.HOR_DIR_AXIS.or((state, side) ->
    {
        Direction facing = state.getValue(FramedProperties.FACING_HOR);
        HorizontalRotation rotation = state.getValue(PropertyHolder.ROTATION);
        return side == rotation.withFacing(facing).getOpposite() ||
               side == rotation.rotate(Rotation.COUNTERCLOCKWISE_90).withFacing(facing).getOpposite();
    });

    public FramedFlatExtendedDoubleSlopePanelCornerBlock(BlockType blockType)
    {
        super(blockType);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(FramedProperties.FACING_HOR, PropertyHolder.ROTATION);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return FramedFlatSlopePanelCornerBlock.getStateForPlacement(this, false, false, context);
    }

    @Override
    public BlockState rotate(BlockState state, Direction face, Rotation rot)
    {
        Direction dir = state.getValue(FramedProperties.FACING_HOR);

        if (Utils.isY(face))
        {
            return state.setValue(FramedProperties.FACING_HOR, rot.rotate(dir));
        }
        else if (face.getAxis() == dir.getAxis())
        {
            HorizontalRotation rotation = state.getValue(PropertyHolder.ROTATION);
            return state.setValue(PropertyHolder.ROTATION, rotation.rotate(rot));
        }

        return state;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState rotate(BlockState state, Rotation rotation) { return rotate(state, Direction.UP, rotation); }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return FramedFlatSlopePanelCornerBlock.mirrorCorner(state, mirror);
    }

    @Override
    protected Tuple<BlockState, BlockState> getBlockPair(BlockState state)
    {
        Direction facing = state.getValue(FramedProperties.FACING_HOR);
        HorizontalRotation rotation = state.getValue(PropertyHolder.ROTATION);
        HorizontalRotation backRot = rotation.rotate(rotation.isVertical() ? Rotation.COUNTERCLOCKWISE_90 : Rotation.CLOCKWISE_90);

        if (getBlockType() == BlockType.FRAMED_FLAT_EXT_INNER_DOUBLE_SLOPE_PANEL_CORNER)
        {
            return new Tuple<>(
                    FBContent.blockFramedFlatExtendedInnerSlopePanelCorner.get()
                            .defaultBlockState()
                            .setValue(FramedProperties.FACING_HOR, facing)
                            .setValue(PropertyHolder.ROTATION, rotation),
                    FBContent.blockFramedFlatSlopePanelCorner.get()
                            .defaultBlockState()
                            .setValue(FramedProperties.FACING_HOR, facing.getOpposite())
                            .setValue(PropertyHolder.ROTATION, backRot)
            );
        }
        else
        {
            return new Tuple<>(
                    FBContent.blockFramedFlatExtendedSlopePanelCorner.get()
                            .defaultBlockState()
                            .setValue(FramedProperties.FACING_HOR, facing)
                            .setValue(PropertyHolder.ROTATION, rotation),
                    FBContent.blockFramedFlatInnerSlopePanelCorner.get()
                            .defaultBlockState()
                            .setValue(FramedProperties.FACING_HOR, facing.getOpposite())
                            .setValue(PropertyHolder.ROTATION, backRot)
            );
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new FramedFlatExtendedDoubleSlopePanelCornerBlockEntity(pos, state);
    }



    public static BlockState itemModelSource()
    {
        return FBContent.blockFramedFlatExtendedDoubleSlopePanelCorner.get()
                .defaultBlockState()
                .setValue(FramedProperties.FACING_HOR, Direction.SOUTH)
                .setValue(PropertyHolder.ROTATION, HorizontalRotation.RIGHT);
    }
}