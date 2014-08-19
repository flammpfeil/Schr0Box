package com.schr0.schr0box.livingutility.entity.chest.ai;

import java.util.List;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;

public class EntityLivingChestAICollectItem_Base extends AIBaseEntityLivingChest
{
    public PathNavigate pathfinder;
    public float speed;
    public double searchRange;
    public double searchHeight;
    public float canCollectRange;

    public EntityItem targetItem;

    private int counter;
    private int catchCounter;
    private boolean avoidsWater;

    public EntityLivingChestAICollectItem_Base(EntityLivingChest_Base livingBaseChest, float moveSpeed)
    {
	super(livingBaseChest);
	this.setMutexBits(3);

	this.pathfinder = livingBaseChest.getNavigator();
	this.speed = moveSpeed;
	this.searchRange = 8.0D;
	this.searchHeight = 1.0D;
	this.canCollectRange = 1.0F;
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	// 初期化
	this.targetItem = null;

	// Listの設定
	List<EntityItem> itemList = (this.theWorld.getEntitiesWithinAABB(EntityItem.class, this.theChest.boundingBox.expand(this.searchRange, this.searchHeight, this.searchRange)));

	// 対象となるアイテムを登録
	this.targetItem = this.getTargetEntityItem(itemList, this.theChest, this.theChest.getHeldItem());

	// theItemに登録されている ＆ お座り状態ではない ＆ インベントリに空きが存在する場合
	if (this.targetItem != null && !this.theChest.isSitting() && !this.theChest.inventory.isFullInventory(this.targetItem.getEntityItem()))
	{
	    return true;
	}

	return false;
    }

    // AIが継続する際の判定
    @Override
    public boolean continueExecuting()
    {
	// ターゲットが登録されていない場合
	if (this.targetItem == null)
	{
	    return false;
	}

	// ターゲットが無くなっていない場合
	if (!this.targetItem.isEntityAlive())
	{
	    return false;
	}

	return true;
    }

    // AIが始まった際に呼ばれる処理
    @Override
    public void startExecuting()
    {
	this.counter = 0;
	this.catchCounter = 0;
	this.avoidsWater = this.theChest.getNavigator().getAvoidsWater();
	this.theChest.getNavigator().setAvoidsWater(false);
    }

    // AIが終了する際に呼ばれる処理
    @Override
    public void resetTask()
    {
	this.pathfinder.clearPathEntity();
	this.theChest.getNavigator().setAvoidsWater(this.avoidsWater);
	this.theChest.setOpen(false);
	this.targetItem = null;
    }

    // AIの処理
    @Override
    public void updateTask()
    {
	// ターゲットを注視
	if (!this.pathfinder.noPath())
	{
	    this.theChest.getLookHelper().setLookPositionWithEntity(this.targetItem, 10.0F, this.theChest.getVerticalFaceSpeed());
	    this.catchCounter = this.catchCounter > 0 ? (this.catchCounter - 1) : 0;
	} else
	{
	    this.catchCounter++;
	}

	// ターゲットに近づく
	if (this.counter == 0)
	{
	    this.pathfinder.tryMoveToXYZ(targetItem.posX, targetItem.posY, targetItem.posZ, this.speed);
	}

	// アイテムを拾う判定
	boolean isCollectItem = false;

	// 拾える範囲まで近づいたらアイテム回収
	if (this.theChest.getDistanceToEntity(targetItem) < this.canCollectRange || 40 < this.catchCounter)
	{
	    if (this.theChest.inventory.addItemStackToInventory(targetItem.getEntityItem()))
	    {
		if (this.targetItem.getEntityItem().stackSize <= 0)
		{
		    this.targetItem.setDead();
		}

		isCollectItem = true;

	    } else
	    {
		targetItem = null;
	    }
	}

	// 蓋の開閉処理
	this.theChest.modelMotion.setCoverMotion(this.theChest, isCollectItem);
	this.counter = (this.counter + 1) % 20;
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // 対象となるアイテムを登録
    public EntityItem getTargetEntityItem(List<EntityItem> itemList, Entity entity, ItemStack itemStack)
    {
	// itemListの範囲を走査
	for (EntityItem entityItem : itemList)
	{
	    ItemStack entityItemStack = entityItem.getEntityItem().copy();

	    if (this.targetItem == null)
	    {
		if (itemStack == null)
		{
		    return entityItem;
		} else
		{
		    if (entityItemStack.isItemEqual(itemStack))
		    {
			// NBTTagが存在している場合
			if (entityItemStack.hasTagCompound() && itemStack.hasTagCompound())
			{
			    if (itemStack.stackTagCompound.equals(entityItemStack.stackTagCompound))
			    {
				return entityItem;
			    }
			} else
			{
			    return entityItem;
			}
		    }
		}
	    } else
	    {
		if (entity.getDistanceSqToEntity(entityItem) < entity.getDistanceSqToEntity(this.targetItem))
		{
		    if (itemStack == null)
		    {
			this.targetItem = entityItem;
		    } else
		    {
			if (entityItemStack.isItemEqual(itemStack))
			{
			    // NBTTagが存在している場合
			    if (entityItemStack.hasTagCompound() && itemStack.hasTagCompound())
			    {
				if (itemStack.stackTagCompound.equals(entityItemStack.stackTagCompound))
				{
				    return entityItem;
				}
			    } else
			    {
				return entityItem;
			    }
			}
		    }
		}
	    }
	}

	return null;
    }

}
