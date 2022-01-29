package xfacthd.framedblocks.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.client.model.data.IModelData;
import xfacthd.framedblocks.client.model.FramedChestLidModel;
import xfacthd.framedblocks.client.util.ClientUtils;
import xfacthd.framedblocks.common.FBContent;
import xfacthd.framedblocks.common.data.ChestState;
import xfacthd.framedblocks.common.data.PropertyHolder;
import xfacthd.framedblocks.common.tileentity.FramedChestTileEntity;

import java.util.EnumMap;
import java.util.Map;

public class FramedChestRenderer extends TileEntityRenderer<FramedChestTileEntity>
{
    private static final RenderType[] RENDER_TYPES = new RenderType[]
            {
                    RenderType.getSolid(),
                    RenderType.getCutout(),
                    RenderType.getCutoutMipped(),
                    RenderType.getTranslucent()
            };
    private static final Map<Direction, IBakedModel> LID_MODELS = new EnumMap<>(Direction.class);

    public FramedChestRenderer(TileEntityRendererDispatcher dispatcher) { super(dispatcher); }

    @Override
    public void render(FramedChestTileEntity te, float partialTicks, MatrixStack matrix, IRenderTypeBuffer buffer, int light, int overlay)
    {
        if (ClientUtils.OPTIFINE_LOADED.get()) { return; }

        BlockState state = te.getBlockState();

        ChestState chestState = state.get(PropertyHolder.CHEST_STATE);
        Direction dir = state.get(PropertyHolder.FACING_HOR);

        long lastChange = te.getLastChangeTime(chestState);

        if (chestState == ChestState.CLOSED) { return; }

        IBakedModel model = LID_MODELS.get(dir);
        //noinspection ConstantConditions
        IModelData data = model.getModelData(te.getWorld(), te.getPos(), state, EmptyModelData.INSTANCE);

        float angle = calculateAngle(te, chestState, dir, lastChange, partialTicks);

        float xOff = dir.getAxis() == Direction.Axis.X ? (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1F/16F : 15F/16F) : 0;
        float zOff = dir.getAxis() == Direction.Axis.Z ? (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1F/16F : 15F/16F) : 0;

        matrix.push();

        matrix.translate(xOff, 9F/16F, zOff);
        matrix.rotate(dir.getAxis() == Direction.Axis.X ? Vector3f.ZP.rotationDegrees(angle) : Vector3f.XN.rotationDegrees(angle));
        matrix.translate(-xOff, -9F/16F, -zOff);

        renderLidModel(te, state, matrix, buffer, model, data);

        matrix.pop();
    }

    private void renderLidModel(FramedChestTileEntity te, BlockState state, MatrixStack matrix, IRenderTypeBuffer buffer, IBakedModel model, IModelData data)
    {
        BlockModelRenderer renderer = Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer();

        for (RenderType type : RENDER_TYPES)
        {
            ForgeHooksClient.setRenderLayer(type);

            //noinspection ConstantConditions
            renderer.renderModelSmooth(
                    te.getWorld(),
                    model,
                    state,
                    te.getPos(),
                    matrix,
                    buffer.getBuffer(type),
                    false,
                    te.getWorld().getRandom(),
                    te.getPos().toLong(),
                    OverlayTexture.NO_OVERLAY,
                    data
            );
        }
        ForgeHooksClient.setRenderLayer(null);
    }

    private float calculateAngle(FramedChestTileEntity te, ChestState chestState, Direction dir, long lastChange, float partialTicks)
    {
        //noinspection ConstantConditions
        float diff = (float) (te.getWorld().getGameTime() - lastChange) + partialTicks;

        float factor = MathHelper.lerp(diff / 10F, 0, 1);
        if (chestState == ChestState.CLOSING) { factor = 1F - factor; }

        factor = 1.0F - factor;
        factor = 1.0F - factor * factor * factor;

        float angle = MathHelper.clamp(factor * 90F, 0F, 90F);
        if (dir.getAxisDirection() == Direction.AxisDirection.NEGATIVE) { angle *= -1F; }

        return angle;
    }



    public static void onModelsLoaded(Map<ResourceLocation, IBakedModel> registry)
    {
        for (Direction dir : Direction.Plane.HORIZONTAL)
        {
            BlockState state = FBContent.blockFramedChest.get().getDefaultState().with(PropertyHolder.FACING_HOR, dir);

            ResourceLocation location = BlockModelShapes.getModelLocation(state);

            LID_MODELS.put(dir, new FramedChestLidModel(
                    state,
                    registry.get(location)
            ));
        }
    }
}