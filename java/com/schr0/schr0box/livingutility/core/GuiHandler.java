package com.schr0.schr0box.livingutility.core;

import com.schr0.schr0box.livingutility.LivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.*;
import com.schr0.schr0box.livingutility.entity.chest.gui.GuiInventoryLivingChestEnder_Collect;
import com.schr0.schr0box.livingutility.entity.chest.gui.GuiInventoryLivingChestEnder_Follow;
import com.schr0.schr0box.livingutility.entity.chest.gui.GuiInventoryLivingChest_Collect;
import com.schr0.schr0box.livingutility.entity.chest.gui.GuiInventoryLivingChest_Follow;
import com.schr0.schr0box.livingutility.entity.chest.inventory.container.ContainerInventoryLivingChestEnder_Follow;
import com.schr0.schr0box.livingutility.entity.chest.inventory.container.ContainerInventoryLivingChest_Collect;
import com.schr0.schr0box.livingutility.entity.chest.inventory.container.ContainerInventoryLivingChest_Follow;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.List;

public class GuiHandler implements IGuiHandler
{
    // サーバー側でGUIが開かれたときに呼ばれる
    // Containerを返す.
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
	if (id == LivingUtility.CHEST_GUI_ID)
	{
	    List list = world.getEntitiesWithinAABB(EntityLivingChest_Base.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));

	    if (!list.isEmpty())
	    {
		if (list.get(0) instanceof EntityLivingChest_Follow)
		{
            if (list.get(0) instanceof EntityLivingChestEnder_Follow) {
                return new ContainerInventoryLivingChestEnder_Follow(player.inventory, (EntityLivingChestEnder_Follow)list.get(0));
            }
		    return new ContainerInventoryLivingChest_Follow(player.inventory, (EntityLivingChest_Follow) list.get(0));
		}

		if (list.get(0) instanceof EntityLivingChest_Collect)
		{
		    return new ContainerInventoryLivingChest_Collect(player.inventory, (EntityLivingChest_Collect) list.get(0));
		}
	    }
	}
        //EnderChest
        if (id == LivingUtility.ENDER_CHEST_GUI_ID) {
            List list = world.getEntitiesWithinAABB(EntityLivingChest_Base.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));

            if (!list.isEmpty() && list.get(0) instanceof EntityLivingChestEnder_Follow) {
                return new ContainerInventoryLivingChestEnder_Follow(player.inventory, (EntityLivingChestEnder_Follow)list.get(0));
            }
        }

	return null;
    }

    // クライアント側で開かれたときに呼ばれる
    // Guiを返す
    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
    {
	if (id == LivingUtility.CHEST_GUI_ID)
	{
	    List list = world.getEntitiesWithinAABB(EntityLivingChest_Base.class, AxisAlignedBB.getBoundingBox(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1));

	    if (!list.isEmpty())
	    {
		if (list.get(0) instanceof EntityLivingChest_Follow)
		{
            if (list.get(0) instanceof EntityLivingChestEnder_Follow) {
                return new GuiInventoryLivingChestEnder_Follow(player.inventory, (EntityLivingChestEnder_Follow)list.get(0));
            }
		    return new GuiInventoryLivingChest_Follow(player.inventory, (EntityLivingChest_Follow) list.get(0));
		}

		if (list.get(0) instanceof EntityLivingChest_Collect)
		{
            if (list.get(0) instanceof EntityLivingChestEnder_Collect) {
                return new GuiInventoryLivingChestEnder_Collect(player.inventory, (EntityLivingChestEnder_Collect)list.get(0));
            }
		    return new GuiInventoryLivingChest_Collect(player.inventory, (EntityLivingChest_Collect) list.get(0));
		}
	    }
	}

	return null;
    }
}
