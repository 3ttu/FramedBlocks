package xfacthd.framedblocks.common.blockentity.doubled;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import xfacthd.framedblocks.api.block.FramedProperties;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.blockentity.FramedDoubleBlockEntity;
import xfacthd.framedblocks.common.data.*;
import xfacthd.framedblocks.api.util.Utils;
import xfacthd.framedblocks.common.data.property.CornerType;

public class FramedDoubleCornerBlockEntity extends FramedDoubleBlockEntity
{
    public FramedDoubleCornerBlockEntity(BlockPos pos, BlockState state)
    {
        super(FBContent.BE_TYPE_DOUBLE_FRAMED_CORNER.get(), pos, state);
    }

    @Override
    protected boolean hitSecondary(BlockHitResult hit)
    {
        CornerType type = getBlockState().getValue(PropertyHolder.CORNER_TYPE);
        Direction facing = getBlockState().getValue(FramedProperties.FACING_HOR);
        Direction side = hit.getDirection();

        Vec3 vec = Utils.fraction(hit.getLocation());

        if (type.isHorizontal())
        {
            if (side == facing || (!type.isTop() && side == Direction.DOWN) || (type.isTop() && side == Direction.UP) ||
                (!type.isRight() && side == facing.getCounterClockWise()) || (type.isRight() && side == facing.getClockWise())
            )
            {
                return false;
            }

            if (side == facing.getOpposite())
            {
                return true;
            }

            if (Utils.isY(side))
            {
                boolean secondary;
                if (type.isRight())
                {
                    secondary = Utils.isX(facing) ? vec.x() >= (1D - vec.z()) : vec.z() >= vec.x();
                }
                else
                {
                   secondary = Utils.isX(facing) ? vec.x() >= vec.z() : vec.z() >= (1D - vec.x());
                }

                if (Utils.isPositive(facing)) { secondary = !secondary; }
                return secondary;
            }
            else if (side == facing.getClockWise() || side == facing.getCounterClockWise())
            {
                double hor = Utils.isX(facing) ? vec.x() : vec.z();
                if (!Utils.isPositive(facing))
                {
                    hor = 1D - hor;
                }

                boolean secondary;
                if (type.isTop())
                {
                    secondary = vec.y() <= (1D - hor);
                }
                else
                {
                    secondary = vec.y() >= hor;
                }

                return secondary;
            }
        }
        else if (type == CornerType.TOP)
        {
            if (side == facing || side == Direction.UP || side == facing.getCounterClockWise())
            {
                return false;
            }
            if (side == Direction.DOWN)
            {
                return true;
            }

            if (side == facing.getClockWise())
            {
                double hor = Utils.isX(facing) ? vec.x() : vec.z();
                if (!Utils.isPositive(facing))
                {
                    hor = 1D - hor;
                }
                return vec.y() <= (1D - hor);
            }
            else if (side == facing.getOpposite())
            {
                Direction dir = facing.getCounterClockWise();
                double hor = Utils.isX(dir) ? vec.x() : vec.z();
                if (!Utils.isPositive(dir))
                {
                    hor = 1D - hor;
                }
                return vec.y() <= (1D - hor);
            }
        }
        else if (type == CornerType.BOTTOM)
        {
            if (side == facing || side == Direction.DOWN || side == facing.getCounterClockWise())
            {
                return false;
            }
            if (side == Direction.UP)
            {
                return true;
            }

            if (side == facing.getClockWise())
            {
                double hor = Utils.isX(facing) ? vec.x() : vec.z();
                if (!Utils.isPositive(facing))
                {
                    hor = 1D - hor;
                }
                return vec.y() >= hor;
            }
            else if (side == facing.getOpposite())
            {
                Direction dir = facing.getCounterClockWise();
                double hor = Utils.isX(dir) ? vec.x() : vec.z();
                if (!Utils.isPositive(dir))
                {
                    hor = 1D - hor;
                }
                return vec.y() >= hor;
            }
        }
        return false;
    }

    @Override
    protected CamoGetter getCamoGetter(Direction side, @Nullable Direction edge)
    {
        CornerType type = getBlockState().getValue(PropertyHolder.CORNER_TYPE);
        Direction dir = getBlockState().getValue(FramedProperties.FACING_HOR);

        if (type.isHorizontal())
        {
            if (side == dir || (!type.isTop() && side == Direction.DOWN) || (type.isTop() && side == Direction.UP) ||
                (!type.isRight() && side == dir.getCounterClockWise()) || (type.isRight() && side == dir.getClockWise())
            )
            {
                return this::getCamo;
            }

            if (side == dir.getOpposite() || (!type.isTop() && side == Direction.UP) || (type.isTop() && side == Direction.DOWN) ||
                (!type.isRight() && side == dir.getClockWise()) || (type.isRight() && side == dir.getCounterClockWise())
            )
            {
                return this::getCamoTwo;
            }
        }
        else if (type == CornerType.TOP)
        {
            if (side == dir || side == Direction.UP || side == dir.getCounterClockWise())
            {
                return this::getCamo;
            }
            if (side == dir.getOpposite() || side == Direction.DOWN || side == dir.getClockWise())
            {
                return this::getCamoTwo;
            }
        }
        else if (type == CornerType.BOTTOM)
        {
            if (side == dir || side == Direction.DOWN || side == dir.getCounterClockWise())
            {
                return this::getCamo;
            }
            if (side == dir.getOpposite() || side == Direction.UP || side == dir.getClockWise())
            {
                return this::getCamoTwo;
            }
        }

        return EMPTY_GETTER;
    }

    @Override
    protected SolidityCheck getSolidityCheck(Direction side)
    {
        CornerType type = getBlockState().getValue(PropertyHolder.CORNER_TYPE);
        Direction facing = getBlockState().getValue(FramedProperties.FACING_HOR);

        if (type.isHorizontal())
        {
            if (side == facing)
            {
                return SolidityCheck.FIRST;
            }
            else if (side == facing.getOpposite())
            {
                return SolidityCheck.SECOND;
            }
            else if ((!type.isRight() && side == facing.getCounterClockWise()) || (type.isRight() && side == facing.getClockWise()))
            {
                return SolidityCheck.FIRST;
            }
            else if ((!type.isTop() && side == Direction.DOWN) || (type.isTop() && side == Direction.UP))
            {
                return SolidityCheck.FIRST;
            }
        }
        else
        {
            if (side == facing || side == facing.getCounterClockWise())
            {
                return SolidityCheck.FIRST;
            }
            else if ((!type.isTop() && side == Direction.DOWN) || (type.isTop() && side == Direction.UP))
            {
                return SolidityCheck.FIRST;
            }
            else if ((!type.isTop() && side == Direction.UP) || (type.isTop() && side == Direction.DOWN))
            {
                return SolidityCheck.SECOND;
            }
        }
        return SolidityCheck.BOTH;
    }
}