package com.schr0.schr0box.livingutility.entity.chest;

import com.schr0.schr0box.livingutility.entity.base.ai.EntityLivingUtilityAILookAtTrader;
import com.schr0.schr0box.livingutility.entity.base.ai.EntityLivingUtilityAITrade;
import com.schr0.schr0box.livingutility.entity.chest.ai.follow.EntityLivingChestAICollectItem_Follow;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EntityLivingChest_Follow extends EntityLivingChest_Base
{
    public EntityLivingChest_Follow(World par1World)
    {
	super(par1World);

	// Entityのサイズ（Y軸, X軸）
	this.setSize(0.9F, 1.35F);

	// 水を避けるかどうかの判定
	this.getNavigator().setAvoidsWater(true);

	// AIの設定
	// 1 水泳 [4]
	// 2 お座り [5]
	// 4 取引中（独自） [none]
	// 4 取引相手を注視（独自）[2]
	// 4 他Entityへの注視 [2]
	// 5 Followのアイテム回収（独自） [3]
	// 6 主人へ追従 [3]
	// 7 自由行動 [3]

	this.tasks.addTask(1, new EntityAISwimming(this));
	this.tasks.addTask(2, this.aiSit);
	this.tasks.addTask(3, new EntityLivingUtilityAITrade(this));
	this.tasks.addTask(3, new EntityLivingUtilityAILookAtTrader(this));
	this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityLiving.class, 6.0F, 0.02F));
	this.tasks.addTask(5, new EntityLivingChestAICollectItem_Follow(this, 1.25F));
	this.tasks.addTask(6, new EntityAIFollowOwner(this, 1.25F, 2.0F, 2.0F));
	this.tasks.addTask(7, new EntityAIWander(this, 1.25F));
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

    // 元となったUtility（ItemStack）のget
    @Override
    public ItemStack getUtility()
    {
	return new ItemStack(Blocks.chest);
    }

}