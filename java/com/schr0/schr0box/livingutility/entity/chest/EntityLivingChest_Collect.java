package com.schr0.schr0box.livingutility.entity.chest;

import com.schr0.schr0box.livingutility.entity.base.ai.EntityLivingUtilityAILookAtTrader;
import com.schr0.schr0box.livingutility.entity.base.ai.EntityLivingUtilityAITrade;
import com.schr0.schr0box.livingutility.entity.chest.ai.collect.EntityLivingChestAICollectItem_Collect;
import com.schr0.schr0box.livingutility.entity.chest.ai.collect.EntityLivingChestAIGoBackHome;
import com.schr0.schr0box.livingutility.entity.chest.ai.collect.EntityLivingChestAITransportInv;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

public class EntityLivingChest_Collect extends EntityLivingChest_Base
{
    public EntityLivingChest_Collect(World par1World)
    {
	super(par1World);

	// Entityのサイズ（Y軸, X軸）
	this.setSize(0.9F, 1.35F);

	// 水を避けるかどうかの判定
	this.getNavigator().setAvoidsWater(true);

	// AIの設定
	// 1 ホームポイントへの帰還[none]
	// 2 水泳 [4]
	// 3 お座り [5]
	// 4 取引中（独自） [none]
	// 4 取引相手を注視（独自）[2]
	// 5 他Entityへの注視 [2]
	// 6 Collectのアイテム回収（独自） [3]
	// 7 インベントリへの輸送（独自） [3]
	// 8 自由行動 [3]

	this.tasks.addTask(1, new EntityLivingChestAIGoBackHome(this, 8, 1, 8));
	this.tasks.addTask(2, new EntityAISwimming(this));
	this.tasks.addTask(3, this.aiSit);
	this.tasks.addTask(4, new EntityLivingUtilityAITrade(this));
	this.tasks.addTask(4, new EntityLivingUtilityAILookAtTrader(this));
	this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityLiving.class, 6.0F, 0.02F));
	this.tasks.addTask(6, new EntityLivingChestAICollectItem_Collect(this, 1.25F));
	this.tasks.addTask(7, new EntityLivingChestAITransportInv(this, 1.25F));
	this.tasks.addTask(8, new EntityAIWander(this, 1.25F));
    }

    // 属性の管理
    @Override
    protected void applyEntityAttributes()
    {
	super.applyEntityAttributes();

	// 最大体力（10.0）
	this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);

	// 移動速度（0.25D）
	this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
    }

    // AIの適用
    @Override
    public boolean isAIEnabled()
    {
	return true;
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // 地点登録のItemStack
    public ItemStack getHomePointItem()
    {
	return this.specialItems.getStackInSlot(0);
    }

    // 登録インベントリの取得
    public IInventory getHomeInventory()
    {
	if (this.getHomePointItem() != null)
	{
	    return TileEntityHopper.func_145893_b(this.worldObj, (double) this.getHomePosX(), (double) this.getHomePosY(), (double) this.getHomePosZ());
	}

	return null;
    }

    // 登録地点の登録名
    public String getHomeName()
    {
	if (this.getHomePointItem() != null)
	{
	    return this.getHomePointItem().getTagCompound().getString("TargetName");
	}

	return "null";
    }

    // 登録地点のX座標
    public int getHomePosX()
    {
	if (this.getHomePointItem() != null)
	{
	    return this.getHomePointItem().getTagCompound().getInteger("TargetPosX");
	}

	return -1;
    }

    // 登録地点のY座標
    public int getHomePosY()
    {
	if (this.getHomePointItem() != null)
	{

	    return this.getHomePointItem().getTagCompound().getInteger("TargetPosY");
	}

	return -1;
    }

    // 登録地点のZ座標
    public int getHomePosZ()
    {
	if (this.getHomePointItem() != null)
	{

	    return this.getHomePointItem().getTagCompound().getInteger("TargetPosZ");
	}

	return -1;
    }

    // 登録地点の面
    public int getHomeSide()
    {
	if (this.getHomePointItem() != null)
	{
	    return this.getHomePointItem().getTagCompound().getInteger("TargetSide");
	}

	return -1;
    }

    // 周囲の登録インベントリ走査（独自）
    public IInventory getNearHomeInventory(double hX, double hY, double hZ, int tX, int tY, int tZ)
    {
	for (int cX = (int) (hX - tX); cX < (hX + tX); ++cX)
	{
	    for (int cY = (int) (hY - tY); cY < (hY + tY); ++cY)
	    {
		for (int cZ = (int) (hZ - tZ); cZ < (hZ + tZ); ++cZ)
		{
		    if (cX == this.getHomePosX() && cY == this.getHomePosY() && cZ == this.getHomePosZ())
		    {
			IInventory inventory = TileEntityHopper.func_145893_b(this.worldObj, (double) cX, (double) cY, (double) cZ);

			if (inventory != null)
			{
			    return inventory;
			}
		    }
		}
	    }
	}

	return null;
    }

}
