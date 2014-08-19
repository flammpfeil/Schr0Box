package com.schr0.schr0box.livingutility.entity.chest.ai;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Base;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class AIBaseEntityLivingChest extends EntityAIBase
{
    public EntityLivingChest_Base theChest;
    public World theWorld;

    public AIBaseEntityLivingChest(EntityLivingChest_Base LivingChest)
    {
	this.theChest = LivingChest;
	this.theWorld = LivingChest.worldObj;
    }

    @Override
    public boolean shouldExecute()
    {
	return false;
    }

}
