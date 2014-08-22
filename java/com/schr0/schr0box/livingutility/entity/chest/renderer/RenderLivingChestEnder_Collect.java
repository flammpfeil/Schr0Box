package com.schr0.schr0box.livingutility.entity.chest.renderer;

import com.schr0.schr0box.livingutility.LivingUtility;
import net.minecraft.util.ResourceLocation;

/**
 * Created by A.K. on 14/08/22.
 */
public class RenderLivingChestEnder_Collect extends RenderLivingChestEnder_Base {
    private static final ResourceLocation COLLECT_TEXTURE = new ResourceLocation(LivingUtility.TEXTURE_DOMAIN + "textures/mobs/nomalchest/collect.png");

    // ResourceLocationのget
    @Override
    public ResourceLocation getResourceLocation()
    {
        return COLLECT_TEXTURE;
    }
}
