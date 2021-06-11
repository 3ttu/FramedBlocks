package xfacthd.framedblocks.common.util;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class FramedMixinConfigPlugin implements IMixinConfigPlugin
{
    @Override
    public void onLoad(String mixinPackage) { }

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName)
    {
        if (mixinClassName.equals("xfacthd.framedblocks.client.util.mixin.MixinWorldRenderer"))
        {
            //return !Utils.SODIUM_LOADED.getValue(); //FIXME: find a better solution for this
            try
            {
                Class.forName("me.jellysquid.mods.sodium.common.config.Option");
                return false;
            }
            catch (ClassNotFoundException e)
            {
                return true;
            }
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) { }

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) { }
}