package xfacthd.framedblocks.client.model.pane;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import xfacthd.framedblocks.api.model.data.QuadMap;
import xfacthd.framedblocks.api.model.geometry.Geometry;
import xfacthd.framedblocks.api.model.wrapping.GeometryFactory;
import xfacthd.framedblocks.api.model.quad.Modifiers;
import xfacthd.framedblocks.api.model.quad.QuadModifier;
import xfacthd.framedblocks.api.util.Utils;

public class FramedHorizontalPaneGeometry implements Geometry
{
    public FramedHorizontalPaneGeometry(@SuppressWarnings("unused") GeometryFactory.Context ctx) { }

    @Override
    public void transformQuad(QuadMap quadMap, BakedQuad quad)
    {
        Direction quadDir = quad.getDirection();
        if (Utils.isY(quadDir))
        {
            QuadModifier.geometry(quad)
                    .apply(Modifiers.setPosition(9F/16F))
                    .export(quadMap.get(null));
        }
        else
        {
            QuadModifier.geometry(quad)
                    .apply(Modifiers.cutSideUpDown(9F/16F))
                    .export(quadMap.get(quadDir));
        }
    }
}
