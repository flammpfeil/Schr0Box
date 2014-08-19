package com.schr0.schr0box.livingutility.entity.chest.ai.collect;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Collect;
import com.schr0.schr0box.livingutility.entity.chest.ai.AIBaseEntityLivingChest;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.tileentity.TileEntityHopper;

public class EntityLivingChestAITransportInv extends AIBaseEntityLivingChest
{
    protected EntityLivingChest_Collect theCollectChest;

    private float speed;
    private float canInsertRange;
    private PathNavigate pathfinder;

    private IInventory targetInventory;
    private int counter;
    private int catchCounter;
    private boolean avoidsWater;

    public EntityLivingChestAITransportInv(EntityLivingChest_Collect livingCollectChest, float moveSpeed)
    {
	super(livingCollectChest);
	this.setMutexBits(3);
	this.theCollectChest = livingCollectChest;

	this.speed = moveSpeed;
	this.canInsertRange = 1.0F;
	this.pathfinder = livingCollectChest.getNavigator();
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	// ターゲットの初期化
	this.targetInventory = null;

	IInventory home = this.theCollectChest.getNearHomeInventory(this.theCollectChest.posX, this.theCollectChest.posY, this.theCollectChest.posZ, 8, 1, 8);

	// 近くに登録インベントリがある場合
	if (home != null)
	{
	    // Inventryを開く
	    this.theCollectChest.inventory.openInventory();
	    home.openInventory();

	    // 自身のインベントリを走査
	    for (int slotM = 0; slotM < this.theCollectChest.inventory.getSizeInventory(); slotM++)
	    {
		// 対象のインベントリを走査
		for (int slotT = 0; slotT < home.getSizeInventory(); slotT++)
		{
		    ItemStack inItem = this.theCollectChest.inventory.getStackInSlot(slotM);

		    if (inItem != null && this.canInsertItems(home, inItem, slotT, this.theCollectChest.getHomeSide()))
		    {
			// 対象インベントリの登録
			this.targetInventory = home;
		    }
		}
	    }

	    // Inventryを閉じる
	    this.theCollectChest.inventory.closeInventory();
	    home.closeInventory();
	}

	// 登録インベントリが存在する場合
	if (this.targetInventory != null)
	{
	    return true;
	}

	return false;
    }

    // AIが継続する際の判定
    @Override
    public boolean continueExecuting()
    {
	boolean flag = false;

	IInventory home = this.theCollectChest.getNearHomeInventory(this.theCollectChest.posX, this.theCollectChest.posY, this.theCollectChest.posZ, 8, 1, 8);

	// 近くに登録インベントリがある場合
	if (home != null)
	{
	    // Inventryを開く
	    this.theCollectChest.inventory.openInventory();
	    home.openInventory();

	    // 自身のインベントリを走査
	    for (int slotM = 0; slotM < this.theCollectChest.inventory.getSizeInventory(); slotM++)
	    {
		// 対象のインベントリを走査
		for (int slotT = 0; slotT < home.getSizeInventory(); slotT++)
		{
		    ItemStack inItem = this.theCollectChest.inventory.getStackInSlot(slotM);

		    if (inItem != null && this.canInsertItems(home, inItem, slotT, this.theCollectChest.getHomeSide()))
		    {
			flag = true;
			this.targetInventory = home;
		    }
		}
	    }

	    // Inventryを閉じる
	    this.theCollectChest.inventory.closeInventory();
	    home.closeInventory();
	}

	return flag;
    }

    // AIが始まった際に呼ばれる処理
    @Override
    public void startExecuting()
    {
	this.counter = 0;
	this.catchCounter = 0;
	this.avoidsWater = this.theCollectChest.getNavigator().getAvoidsWater();
	this.theCollectChest.getNavigator().setAvoidsWater(false);
    }

    // AIが終了する際に呼ばれる処理
    @Override
    public void resetTask()
    {
	this.pathfinder.clearPathEntity();
	this.theCollectChest.getNavigator().setAvoidsWater(this.avoidsWater);
	this.theCollectChest.setOpen(false);
	this.targetInventory = null;
    }

    // AIの処理
    @Override
    public void updateTask()
    {
	// ターゲットを注視する
	if (!this.pathfinder.noPath())
	{
	    this.theCollectChest.getLookHelper().setLookPosition(this.theCollectChest.getHomePosX(), this.theCollectChest.getHomePosY(), this.theCollectChest.getHomePosZ(), 10.0F, this.theCollectChest.getVerticalFaceSpeed());
	    this.catchCounter = this.catchCounter > 0 ? (this.catchCounter - 1) : 0;
	} else
	{
	    this.catchCounter++;
	}

	// ターゲットに近づく
	if (this.counter == 0)
	{
	    if (this.canInsertRange < this.theCollectChest.getDistanceSq(this.theCollectChest.getHomePosX(), this.theCollectChest.getHomePosY(), this.theCollectChest.getHomePosZ()))
	    {
		this.theCollectChest.getNavigator().tryMoveToXYZ(this.theCollectChest.getHomePosX(), this.theCollectChest.getHomePosY(), this.theCollectChest.getHomePosZ(), this.speed);
	    }
	}

	// ターゲットに収納する判定
	boolean isInsertChest = false;

	// アイテム収納
	if (this.theCollectChest.getDistance(this.theCollectChest.getHomePosX(), this.theCollectChest.getHomePosY(), this.theCollectChest.getHomePosZ()) < this.canInsertRange || 40 < this.catchCounter)
	{
	    // Inventryを開く
	    this.theCollectChest.inventory.openInventory();
	    this.targetInventory.openInventory();

	    // 自身のインベントリを走査
	    for (int slot = 0; slot < this.theCollectChest.inventory.getSizeInventory(); slot++)
	    {
		ItemStack inItem = this.theCollectChest.inventory.getStackInSlot(slot);

		if (inItem != null)
		{
		    ItemStack insertItem = TileEntityHopper.func_145889_a(this.targetInventory, inItem, this.theCollectChest.getHomeSide());

		    if (insertItem == null || insertItem.stackSize == 0)
		    {
			this.theCollectChest.inventory.containerItems[slot] = null;
			isInsertChest = true;
		    }
		}
	    }

	    // Inventryを閉じる
	    this.theCollectChest.inventory.closeInventory();
	    this.targetInventory.closeInventory();
	}

	// 蓋の開閉処理
	this.theCollectChest.modelMotion.setCoverMotion(this.theCollectChest, isInsertChest);
	this.counter = (this.counter + 1) % 20;
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // インベントリ搬入の判定（TileEntityHopper）
    private boolean canInsertItems(IInventory par1IInventory, ItemStack par2ItemStack, int slot, int side)
    {
	boolean flag = false;

	ItemStack slotItemStack = par1IInventory.getStackInSlot(slot);

	if (this.canInsertItemToInventory(par1IInventory, par2ItemStack, slot, side))
	{
	    if (slotItemStack == null)
	    {
		int max = Math.min(par2ItemStack.getMaxStackSize(), par1IInventory.getInventoryStackLimit());

		if (max >= par2ItemStack.stackSize)
		{
		    flag = true;
		}
	    } else if (this.areItemStacksEqualItem(slotItemStack, par2ItemStack))
	    {
		int max = Math.min(par2ItemStack.getMaxStackSize(), par1IInventory.getInventoryStackLimit());

		if (max > slotItemStack.stackSize)
		{
		    flag = 0 < Math.min(par2ItemStack.stackSize, max - slotItemStack.stackSize);
		}
	    }
	}

	return flag;
    }

    private boolean canInsertItemToInventory(IInventory par1IInventory, ItemStack par2ItemStack, int slot, int side)
    {
	return !par1IInventory.isItemValidForSlot(slot, par2ItemStack) ? false : !(par1IInventory instanceof ISidedInventory) || ((ISidedInventory) par1IInventory).canInsertItem(slot, par2ItemStack, side);
    }

    private boolean areItemStacksEqualItem(ItemStack par1ItemStack, ItemStack par2ItemStack)
    {
	return par1ItemStack.getItem() != par2ItemStack.getItem() ? false : (par1ItemStack.getItemDamage() != par2ItemStack.getItemDamage() ? false : (par1ItemStack.stackSize > par1ItemStack.getMaxStackSize() ? false : ItemStack.areItemStackTagsEqual(par1ItemStack, par2ItemStack)));
    }

}
