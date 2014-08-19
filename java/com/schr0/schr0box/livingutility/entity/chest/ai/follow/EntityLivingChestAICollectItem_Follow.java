package com.schr0.schr0box.livingutility.entity.chest.ai.follow;

import java.util.List;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Follow;
import com.schr0.schr0box.livingutility.entity.chest.ai.EntityLivingChestAICollectItem_Base;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;

public class EntityLivingChestAICollectItem_Follow extends EntityLivingChestAICollectItem_Base
{
    protected EntityLivingChest_Follow theFollowChest;

    public EntityLivingChestAICollectItem_Follow(EntityLivingChest_Follow livingFollowChest, float moveSpeed)
    {
	super(livingFollowChest, moveSpeed);
	this.theFollowChest = livingFollowChest;
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	// 初期化
	this.targetItem = null;

	// 主人が存在する場合
	if (this.theFollowChest.getOwner() != null)
	{
	    int hX = (int) this.theFollowChest.getOwner().posX;
	    int hY = (int) this.theFollowChest.getOwner().posY;
	    int hZ = (int) this.theFollowChest.getOwner().posZ;

	    // Listの設定
	    List<EntityItem> itemList = this.theWorld.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(hX - this.searchRange, hY - this.searchRange, hZ - this.searchRange, hX + this.searchRange, hY + this.searchRange, hZ + this.searchRange));

	    // 対象となるアイテムを登録
	    this.targetItem = this.getTargetEntityItem(itemList, this.theFollowChest, this.theFollowChest.getHeldItem());

	    // theItemに登録されている ＆ お座り状態ではない ＆ インベントリに空きが存在する場合
	    if (this.targetItem != null && !this.theFollowChest.isSitting() && !this.theFollowChest.inventory.isFullInventory(this.targetItem.getEntityItem()))
	    {
		return true;
	    }

	    return false;
	}

	return super.shouldExecute();
    }
}
