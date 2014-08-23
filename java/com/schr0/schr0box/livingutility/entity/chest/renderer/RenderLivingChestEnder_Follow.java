package com.schr0.schr0box.livingutility.entity.chest.renderer;

import com.schr0.schr0box.livingutility.LivingUtility;
import net.minecraft.util.ResourceLocation;

/**
 * Created by A.K. on 14/08/22.
 */
public class RenderLivingChestEnder_Follow extends RenderLivingChestEnder_Base {
    private static final ResourceLocation FOLLOW_TEXTURE = new ResourceLocation(LivingUtility.TEXTURE_DOMAIN + "textures/mobs/nomalchest/follow.png");

    // ResourceLocationのget
    @Override
    public ResourceLocation getResourceLocation()
    {
        return FOLLOW_TEXTURE;
    }
}
