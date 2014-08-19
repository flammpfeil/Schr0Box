package com.schr0.schr0box.livingutility.entity.chest.ai.collect;

import java.util.List;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Collect;
import com.schr0.schr0box.livingutility.entity.chest.ai.EntityLivingChestAICollectItem_Base;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.AxisAlignedBB;

public class EntityLivingChestAICollectItem_Collect extends EntityLivingChestAICollectItem_Base
{
    protected EntityLivingChest_Collect theCollectChest;

    public EntityLivingChestAICollectItem_Collect(EntityLivingChest_Collect livingCollectChest, float moveSpeed)
    {
	super(livingCollectChest, moveSpeed);
	this.theCollectChest = livingCollectChest;
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	// 初期化
	this.targetItem = null;

	// 登録インベントリがある場合
	if (this.theCollectChest.getHomeInventory() != null)
	{
	    int hX = this.theCollectChest.getHomePosX();
	    int hY = this.theCollectChest.getHomePosY();
	    int hZ = this.theCollectChest.getHomePosZ();

	    // Listの設定
	    List<EntityItem> itemList = this.theWorld.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(hX - this.searchRange, hY - this.searchRange, hZ - this.searchRange, hX + this.searchRange, hY + this.searchRange, hZ + this.searchRange));

	    // 対象となるアイテムを登録
	    this.targetItem = this.getTargetEntityItem(itemList, this.theCollectChest, this.theCollectChest.getHeldItem());

	    // theItemに登録されている ＆ お座り状態ではない ＆ インベントリに空きが存在する場合
	    if (this.targetItem != null && !this.theCollectChest.isSitting() && !this.theCollectChest.inventory.isFullInventory(this.targetItem.getEntityItem()))
	    {
		return true;
	    }

	    return false;
	}

	return super.shouldExecute();
    }

}