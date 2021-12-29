package xfacthd.framedblocks.common.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.util.DoubleSoundMode;

public class FramedDoubleSlabTileEntity extends FramedDoubleTileEntity
{
    public FramedDoubleSlabTileEntity() { super(FBContent.tileTypeDoubleFramedSlab.get()); }

    @Override
    protected boolean hitSecondary(BlockRayTraceResult hit)
    {
        return hit.getFace() == Direction.UP || MathHelper.frac(hit.getHitVec().getY()) >= .5F;
    }

    @Override
    public DoubleSoundMode getSoundMode() { return DoubleSoundMode.SECOND; }

    @Override
    public BlockState getCamoState(Direction side)
    {
        if (side == Direction.UP) { return getCamoStateTwo(); }
        if (side == Direction.DOWN) { return getCamoState(); }
        return Blocks.AIR.getDefaultState();
    }

    @Override
    public boolean isSolidSide(Direction side)
    {
        if (side.getAxis() == Direction.Axis.Y)
        {
            return getCamoState(side).isSolid();
        }
        return getCamoState().isSolid() && getCamoStateTwo().isSolid();
    }
}