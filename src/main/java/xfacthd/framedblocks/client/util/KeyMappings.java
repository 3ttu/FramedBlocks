package xfacthd.framedblocks.client.util;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;
import xfacthd.framedblocks.api.block.FramedBlockEntity;
import xfacthd.framedblocks.api.util.FramedConstants;

@Mod.EventBusSubscriber(modid = FramedConstants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class KeyMappings
{
    public static final String KEY_CATEGORY = FramedConstants.MOD_ID + ".key.categories.framedblocks";
    public static final Lazy<KeyMapping> KEYMAPPING_UPDATE_CULLING = makeKeyMapping("update_cull", GLFW.GLFW_KEY_F9);

    @SubscribeEvent
    public static void register(final RegisterKeyMappingsEvent event)
    {
        event.register(KEYMAPPING_UPDATE_CULLING.get());

        MinecraftForge.EVENT_BUS.addListener(KeyMappings::onClientTick);
    }

    private static Lazy<KeyMapping> makeKeyMapping(String name, int key)
    {
        return Lazy.of(() ->
                new KeyMapping(FramedConstants.MOD_ID + ".key." + name, key, KEY_CATEGORY)
        );
    }

    private static void onClientTick(final TickEvent.ClientTickEvent event)
    {
        Level level = Minecraft.getInstance().level;
        if (event.phase != TickEvent.Phase.START || level == null || Minecraft.getInstance().screen != null) { return; }

        if (KEYMAPPING_UPDATE_CULLING.get().consumeClick())
        {
            HitResult hit = Minecraft.getInstance().hitResult;
            if (hit instanceof BlockHitResult blockHit && level.getBlockEntity(blockHit.getBlockPos()) instanceof FramedBlockEntity be)
            {
                be.updateCulling(true, true);

                BlockPos pos = blockHit.getBlockPos();
                String msg = String.format("Culling updated at {x=%d, y=%d, z=%d}", pos.getX(), pos.getY(), pos.getZ());

                //noinspection ConstantConditions
                Minecraft.getInstance().player.displayClientMessage(Component.literal(msg), true);
            }
        }
    }



    private KeyMappings() { }
}
