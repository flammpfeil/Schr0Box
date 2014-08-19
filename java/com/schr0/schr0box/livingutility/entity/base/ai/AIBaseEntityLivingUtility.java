package com.schr0.schr0box.livingutility.entity.base.ai;

import com.schr0.schr0box.livingutility.entity.base.EntityLivingUtility;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.world.World;

public class AIBaseEntityLivingUtility extends EntityAIBase
{
    public EntityLivingUtility theUtility;
    public World theWorld;

    public AIBaseEntityLivingUtility(EntityLivingUtility livingUtility)
    {
	this.theUtility = livingUtility;
	this.theWorld = livingUtility.worldObj;
    }

    @Override
    public boolean shouldExecute()
    {
	return false;
    }

}
