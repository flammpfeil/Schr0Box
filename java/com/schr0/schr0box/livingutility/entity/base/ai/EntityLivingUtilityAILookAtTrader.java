package com.schr0.schr0box.livingutility.entity.base.ai;

import com.schr0.schr0box.livingutility.entity.base.EntityLivingUtility;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.world.World;

public class EntityLivingUtilityAILookAtTrader extends EntityAIWatchClosest
{
    public EntityLivingUtility theUtility;
    public World theWorld;

    public EntityLivingUtilityAILookAtTrader(EntityLivingUtility livingUtility)
    {
	super(livingUtility, Entity.class, 8.0F);
	this.theUtility = livingUtility;
	this.theWorld = livingUtility.worldObj;
    }

    // AIの始まる判定
    @Override
    public boolean shouldExecute()
    {
	if (this.theUtility.isTrading())
	{
	    this.closestEntity = this.theUtility.getCustomer();
	    return true;
	} else
	{
	    return false;
	}
    }
}
