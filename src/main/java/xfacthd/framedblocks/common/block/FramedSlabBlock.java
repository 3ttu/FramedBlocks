package xfacthd.framedblocks.common.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.BlockType;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.tileentity.FramedDoubleTileEntity;
import xfacthd.framedblocks.common.tileentity.FramedTileEntity;
import xfacthd.framedblocks.common.util.CtmPredicate;
import xfacthd.framedblocks.common.util.SideSkipPredicate;

@SuppressWarnings("deprecation")
public class FramedSlabBlock extends FramedBlock
{
    public static final CtmPredicate CTM_PREDICATE = (state, dir) ->
            (state.get(PropertyHolder.TOP) && dir == Direction.UP) ||
            (!state.get(PropertyHolder.TOP) && dir == Direction.DOWN);

    public static final SideSkipPredicate SKIP_PREDICATE = (world, pos, state, adjState, side) ->
    {
        if (side.getAxis() == Direction.Axis.Y)
        {
            return SideSkipPredicate.CTM.test(world, pos, state, adjState, side);
        }

        if (adjState.getBlock() == FBContent.blockFramedSlab)
        {
            boolean top = state.get(PropertyHolder.TOP);
            if (top != adjState.get(PropertyHolder.TOP)) { return false; }

            return SideSkipPredicate.compareState(world, pos, side, top ? Direction.UP : Direction.DOWN);
        }

        if (adjState.getBlock() == FBContent.blockFramedDoubleSlab)
        {
            TileEntity te = world.getTileEntity(pos.offset(side));
            if (!(te instanceof FramedDoubleTileEntity)) { return false; }
            FramedDoubleTileEntity tile = (FramedDoubleTileEntity) te;

            Direction face = state.get(PropertyHolder.TOP) ? Direction.UP : Direction.DOWN;
            return SideSkipPredicate.compareState(world, pos, tile.getCamoState(face), face);
        }

        if (adjState.getBlock() == FBContent.blockFramedSlabEdge)
        {
            boolean top = state.get(PropertyHolder.TOP);
            if (top != adjState.get(PropertyHolder.TOP)) { return false; }
            if (adjState.get(PropertyHolder.FACING_HOR) != side.getOpposite()) { return false; }

            return SideSkipPredicate.compareState(world, pos, side, top ? Direction.UP : Direction.DOWN);
        }

        return false;
    };

    public FramedSlabBlock()
    {
        super("framed_slab", BlockType.FRAMED_SLAB);
        setDefaultState(getDefaultState().with(PropertyHolder.TOP, false));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
    {
        builder.add(PropertyHolder.TOP, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context)
    {
        return withWater(withTop(getDefaultState(), context.getFace(), context.getHitVec()), context.getWorld(), context.getPos());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() == FBContent.blockFramedSlab.asItem())
        {
            boolean top = state.get(PropertyHolder.TOP);
            Direction face = hit.getFace();
            if ((face == Direction.UP && !top) || (face == Direction.DOWN && top))
            {
                if (!world.isRemote())
                {
                    BlockState camoState = Blocks.AIR.getDefaultState();
                    ItemStack camoStack = ItemStack.EMPTY;
                    boolean glowing = false;

                    TileEntity te = world.getTileEntity(pos);
                    if (te instanceof FramedTileEntity)
                    {
                        camoState = ((FramedTileEntity) te).getCamoState();
                        camoStack = ((FramedTileEntity) te).getCamoStack();
                        glowing = ((FramedTileEntity) te).isGlowing();
                    }

                    world.setBlockState(pos, FBContent.blockFramedDoubleSlab.getDefaultState());

                    SoundType sound = FBContent.blockFramedCube.getSoundType(FBContent.blockFramedCube.getDefaultState());
                    world.playSound(null, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1.0F) / 2.0F, sound.getPitch() * 0.8F);

                    if (!player.isCreative())
                    {
                        stack.shrink(1);
                        player.inventory.markDirty();
                    }

                    te = world.getTileEntity(pos);
                    if (te instanceof FramedDoubleTileEntity)
                    {
                        ((FramedDoubleTileEntity) te).setCamo(camoStack, camoState, top);
                        ((FramedDoubleTileEntity) te).setGlowing(glowing);
                    }
                }
                return ActionResultType.func_233537_a_(world.isRemote());
            }
        }
        return super.onBlockActivated(state, world, pos, player, hand, hit);
    }

    public static ImmutableMap<BlockState, VoxelShape> generateShapes(ImmutableList<BlockState> states)
    {
        VoxelShape bottomShape = makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
        VoxelShape topShape = makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);

        ImmutableMap.Builder<BlockState, VoxelShape> builder = ImmutableMap.builder();

        for (BlockState state : states)
        {
            builder.put(state, state.get(PropertyHolder.TOP) ? topShape : bottomShape);
        }

        return builder.build();
    }
}