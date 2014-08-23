package com.schr0.schr0box.livingutility.proxy;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChestEnder_Collect;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChestEnder_Follow;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Collect;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Follow;
import com.schr0.schr0box.livingutility.entity.chest.renderer.RenderLivingChestEnder_Collect;
import com.schr0.schr0box.livingutility.entity.chest.renderer.RenderLivingChestEnder_Follow;
import com.schr0.schr0box.livingutility.entity.chest.renderer.RenderLivingChest_Collect;
import com.schr0.schr0box.livingutility.entity.chest.renderer.RenderLivingChest_Follow;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy
{
    @Override
    public void registerClient()
    {
	// Entityのクラスと描画, モデルを結びつける //
	RenderingRegistry.registerEntityRenderingHandler(EntityLivingChest_Follow.class, new RenderLivingChest_Follow());
	RenderingRegistry.registerEntityRenderingHandler(EntityLivingChest_Collect.class, new RenderLivingChest_Collect());
        //EnderChest登録
        RenderingRegistry.registerEntityRenderingHandler(EntityLivingChestEnder_Follow.class, new RenderLivingChestEnder_Follow());
        RenderingRegistry.registerEntityRenderingHandler(EntityLivingChestEnder_Collect.class, new RenderLivingChestEnder_Collect());
    }
}
