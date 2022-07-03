package xfacthd.framedblocks.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import xfacthd.framedblocks.api.data.CamoContainer;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.data.*;
import xfacthd.framedblocks.common.blockentity.FramedDoubleSlopeBlockEntity;
import xfacthd.framedblocks.common.blockentity.FramedDoubleBlockEntity;
import xfacthd.framedblocks.api.util.CtmPredicate;
import xfacthd.framedblocks.common.data.property.SlopeType;

import javax.annotation.Nullable;

public class FramedDoubleSlopeBlock extends AbstractFramedDoubleBlock
{
    public static final CtmPredicate CTM_PREDICATE = (state, dir) ->
    {
        if (state.getValue(PropertyHolder.SLOPE_TYPE) == SlopeType.HORIZONTAL)
        {
            return dir != null && !Utils.isY(dir);
        }
        else
        {
            Direction facing = state.getValue(PropertyHolder.FACING_HOR);
            return (dir != null && Utils.isY(dir)) || dir == facing || dir == facing.getOpposite();
        }
    };

    public FramedDoubleSlopeBlock() { super(BlockType.FRAMED_DOUBLE_SLOPE); }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        super.createBlockStateDefinition(builder);
        builder.add(PropertyHolder.FACING_HOR, PropertyHolder.SLOPE_TYPE);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        return withSlopeType(defaultBlockState(), context.getClickedFace(), context.getHorizontalDirection(), context.getClickLocation());
    }

    @Override
    @SuppressWarnings("deprecation")
    public SoundType getCamoSound(BlockState state, LevelReader level, BlockPos pos)
    {
        SlopeType type = state.getValue(PropertyHolder.SLOPE_TYPE);
        if (type != SlopeType.HORIZONTAL)
        {
            if (level.getBlockEntity(pos) instanceof FramedDoubleBlockEntity dbe)
            {
                CamoContainer camo = type == SlopeType.TOP ? dbe.getCamo() : dbe.getCamoTwo();
                if (!camo.isEmpty())
                {
                    return camo.getSoundType();
                }

                camo = type == SlopeType.TOP ? dbe.getCamoTwo() : dbe.getCamo();
                if (!camo.isEmpty())
                {
                    return camo.getSoundType();
                }
            }
            return getSoundType(state);
        }
        return super.getCamoSound(state, level, pos);
    }

    @Override
    public final BlockEntity newBlockEntity(BlockPos pos, BlockState state)
    {
        return new FramedDoubleSlopeBlockEntity(pos, state);
    }
}