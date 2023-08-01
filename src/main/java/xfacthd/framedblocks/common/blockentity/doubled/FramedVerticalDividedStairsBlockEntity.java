package xfacthd.framedblocks.common.blockentity.doubled;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.blockentity.FramedDoubleBlockEntity;

public class FramedVerticalDividedStairsBlockEntity extends FramedDoubleBlockEntity
{
    public FramedVerticalDividedStairsBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_FRAMED_VERTICAL_DIVIDED_STAIRS.get(), pos, state);
    }

    @Override
    protected boolean hitSecondary(BlockHitResult hit)
    {
        Direction side = hit.getDirection();
        if (side == Direction.UP)
        {
            return true;
        }
        if (side == Direction.DOWN)
        {
            return false;
        }
        return Utils.fraction(hit.getLocation()).y > .5;
    }

    @Override
    protected CamoGetter getCamoGetter(Direction side, @Nullable Direction edge)
    {
        Direction facing = getBlockState().getValue(FramedProperties.FACING_HOR);
        if (edge == facing || edge == facing.getCounterClockWise())
        {
            return switch (side)
            {
                case UP -> this::getCamoTwo;
                case DOWN -> this::getCamo;
                default -> EMPTY_GETTER;
            };
        }
        return EMPTY_GETTER;
    }

    @Override
    protected SolidityCheck getSolidityCheck(Direction side)
    {
        Direction facing = getBlockState().getValue(FramedProperties.FACING_HOR);
        if (side == facing || side == facing.getCounterClockWise())
        {
            return SolidityCheck.BOTH;
        }
        return SolidityCheck.NONE;
    }
}