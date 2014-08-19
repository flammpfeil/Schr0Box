package com.schr0.schr0box.livingutility.entity.base.ai;

import com.schr0.schr0box.livingutility.entity.base.EntityLivingUtility;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class EntityLivingUtilityAITrade extends AIBaseEntityLivingUtility
{
    public EntityLivingUtilityAITrade(EntityLivingUtility livingUtility)
    {
	super(livingUtility);
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	if (!this.theUtility.isEntityAlive())
	{
	    return false;
	} else if (this.theUtility.isInWater())
	{
	    return false;
	} else if (!this.theUtility.onGround)
	{
	    return false;
	} else if (this.theUtility.velocityChanged)
	{
	    return false;
	} else
	{
	    EntityPlayer player = this.theUtility.getCustomer();
	    return player == null ? false : (this.theUtility.getDistanceSqToEntity(player) > 16.0D ? false : player.openContainer instanceof Container);
	}
    }

    // AIが始まった際に呼ばれる処理
    @Override
    public void startExecuting()
    {
	this.theUtility.getNavigator().clearPathEntity();
    }

    // AIが終了する際に呼ばれる処理
    @Override
    public void resetTask()
    {
	this.theUtility.setCustomer((EntityPlayer) null);
    }
}
