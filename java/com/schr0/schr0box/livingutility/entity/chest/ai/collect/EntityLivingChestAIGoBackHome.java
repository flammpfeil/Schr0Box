package com.schr0.schr0box.livingutility.entity.chest.ai.collect;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Collect;
import com.schr0.schr0box.livingutility.entity.chest.ai.AIBaseEntityLivingChest;

import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class EntityLivingChestAIGoBackHome extends AIBaseEntityLivingChest
{
    protected EntityLivingChest_Collect theCollectChest;
    private int rangeX;
    private int rangeY;
    private int rangeZ;

    public EntityLivingChestAIGoBackHome(EntityLivingChest_Collect livingCollectChest, int rX, int rY, int rZ)
    {
	super(livingCollectChest);
	this.theCollectChest = livingCollectChest;
	this.rangeX = rX;
	this.rangeY = rY;
	this.rangeZ = rZ;
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	return this.theCollectChest.getNearHomeInventory(this.theCollectChest.posX, this.theCollectChest.posY, this.theCollectChest.posZ, this.rangeX, this.rangeY, this.rangeZ) == null;
    }

    // AIが継続する際の判定
    @Override
    public boolean continueExecuting()
    {
	return this.theCollectChest.getNearHomeInventory(this.theCollectChest.posX, this.theCollectChest.posY, this.theCollectChest.posZ, this.rangeX, this.rangeY, this.rangeZ) == null;
    }

    // AIの処理
    @Override
    public void updateTask()
    {
	int hX = MathHelper.floor_double(this.theCollectChest.getHomePosX());
	int hY = MathHelper.floor_double(this.theCollectChest.getHomePosY());
	int hZ = MathHelper.floor_double(this.theCollectChest.getHomePosZ());

	// ワープ可能な位置の走査
	for (int i = 0; i <= 4; ++i)
	{
	    for (int j = 0; j <= 4; ++j)
	    {
		if ((i < 1 || j < 1 || i > 3 || j > 3) && World.doesBlockHaveSolidTopSurface(this.theWorld, hX + i, hY - 1, hZ + j) && !this.theWorld.getBlock(hX + i, hY, hZ + j).isNormalCube() && !this.theWorld.getBlock(hX + i, hY + 1, hZ + j).isNormalCube())
		{
		    this.theCollectChest.setLocationAndAngles(hX + i + 0.5F, hY, hZ + j + 0.5F, this.theCollectChest.rotationYaw, this.theCollectChest.rotationPitch);
		    return;
		}
	    }
	}

    }
}
