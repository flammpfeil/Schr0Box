package com.schr0.schr0box.livingutility.entity.chest.renderer;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Base;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * Created by A.K. on 14/08/22.
 */
public abstract class RenderLivingChestEnder_Base extends RenderLivingChest_Base {
    private static final ResourceLocation ENDER_CHEST_TEXTURE = new ResourceLocation("textures/entity/chest/ender.png");
    // レンダーパス（EntityLivingBase）
    @Override
    protected int shouldRenderPass(EntityLivingBase par1Entity, int par2, float par3)
    {
        return this.shouldRenderPass((EntityLivingChest_Base) par1Entity, par2, par3);
    }

    // レンダーパス（EntityLivingChest_Base）
    private int shouldRenderPass(EntityLivingChest_Base par1Entity, int par2, float par3)
    {
        if (par2 == 0)
        {
            this.bindTexture(ENDER_CHEST_TEXTURE);
            return 1;
        } else
        {
            return -1;
        }
    }
}
