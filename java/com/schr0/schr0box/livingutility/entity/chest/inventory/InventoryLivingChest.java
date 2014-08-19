package com.schr0.schr0box.livingutility.entity.chest.inventory;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest_Base;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class InventoryLivingChest implements IInventory
{
    // インベントリのItemstack
    public ItemStack[] containerItems = new ItemStack[27];

    public EntityLivingChest_Base livingBaseChest;

    public InventoryLivingChest(EntityLivingChest_Base par1EntityLivingChest)
    {
	this.livingBaseChest = par1EntityLivingChest;
    }

    // インベントリのSize
    @Override
    public int getSizeInventory()
    {
	return this.containerItems.length;
    }

    // 中身のItemStack
    @Override
    public ItemStack getStackInSlot(int par1)
    {
	return this.containerItems[par1];
    }

    // ???
    @Override
    public ItemStack decrStackSize(int par1, int par2)
    {
	if (this.containerItems[par1] != null)
	{
	    ItemStack itemstack;

	    if (this.containerItems[par1].stackSize <= par2)
	    {
		itemstack = this.containerItems[par1];
		this.containerItems[par1] = null;
		return itemstack;
	    } else
	    {
		itemstack = this.containerItems[par1].splitStack(par2);

		if (this.containerItems[par1].stackSize == 0)
		{
		    this.containerItems[par1] = null;
		}

		return itemstack;
	    }
	} else
	{
	    return null;
	}
    }

    // Slotから読み込む中身のItemStack
    @Override
    public ItemStack getStackInSlotOnClosing(int par1)
    {
	if (this.containerItems[par1] != null)
	{
	    ItemStack itemstack = this.containerItems[par1];
	    this.containerItems[par1] = null;
	    return itemstack;
	} else
	{
	    return null;
	}
    }

    // インベントリへの搬入
    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack)
    {
	this.containerItems[par1] = par2ItemStack;

	if (par2ItemStack != null && par2ItemStack.stackSize > this.getInventoryStackLimit())
	{
	    par2ItemStack.stackSize = this.getInventoryStackLimit();
	}
    }

    // インベントリの名称
    @Override
    public String getInventoryName()
    {
	return this.livingBaseChest.getCommandSenderName();
    }

    // インベントリ名が変更可能かの判定
    @Override
    public boolean hasCustomInventoryName()
    {
	return true;
    }

    // 搬入されるItemStackの最大数
    @Override
    public int getInventoryStackLimit()
    {
	return 64;
    }

    // 中身が変化する際に呼ばれる
    @Override
    public void markDirty()
    {
	// 内部インベントリの保存
	this.save();
    }

    // インベントリを開ける際の条件
    @Override
    public boolean isUseableByPlayer(EntityPlayer par1EntityPlayer)
    {
	return this.livingBaseChest.isDead ? false : par1EntityPlayer.getDistanceSqToEntity(this.livingBaseChest) <= 64.0D;
    }

    // 開く際に呼ばれる
    @Override
    public void openInventory()
    {
	// 開く
	this.livingBaseChest.setOpen(true);

	// 内部インベントリの読み込み
	this.load();
    }

    // 閉じる際に呼ばれる
    @Override
    public void closeInventory()
    {
	// 閉じる
	this.livingBaseChest.setOpen(false);

	// 内部インベントリの保存
	this.save();
    }

    // 搬入可能なItemStackの判定
    @Override
    public boolean isItemValidForSlot(int par1, ItemStack par2ItemStack)
    {
	return true;
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // インベントリの保存
    public void save()
    {
	NBTTagList nbttaglist = new NBTTagList();
	for (int i = 0; i < this.containerItems.length; i++)
	{
	    if (this.containerItems[i] != null)
	    {
		NBTTagCompound nbttagcompound1 = new NBTTagCompound();
		nbttagcompound1.setByte("InventorySlot", (byte) i);
		this.containerItems[i].writeToNBT(nbttagcompound1);
		nbttaglist.appendTag(nbttagcompound1);
	    }
	}

	// ItemStackのNBTに中身を保存
	NBTTagCompound nbttagcompound = this.livingBaseChest.getEntityData();
	if (nbttagcompound == null)
	{
	    nbttagcompound = new NBTTagCompound();
	}

	nbttagcompound.setTag("InventoryItems", nbttaglist);
    }

    // インベントリの読み込み
    public void load()
    {
	// ItemStackのNBTを取得、空の中身を作成しておく
	NBTTagCompound nbttagcompound = this.livingBaseChest.getEntityData();
	this.containerItems = new ItemStack[this.getSizeInventory()];

	// NBTが無ければ中身は空のままで
	if (nbttagcompound == null)
	{
	    return;
	}

	NBTTagList nbttaglist = nbttagcompound.getTagList("InventoryItems", 10);
	for (int i = 0; i < nbttaglist.tagCount(); i++)
	{
	    NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
	    int j = nbttagcompound1.getByte("InventorySlot") & 0xff;

	    if (j >= 0 && j < this.containerItems.length)
	    {
		this.containerItems[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
	    }
	}
    }

    // インベントリが一杯の場合の判定
    public boolean isFullInventory(ItemStack par1ItemStack)
    {
	return (this.getFirstEmptyStack() == -1 && this.storeItemStack(par1ItemStack) == -1);
    }

    // 最初の空きスロットを取得（InventoryPlayer）
    public int getFirstEmptyStack()
    {
	for (int i = 0; i < this.getSizeInventory(); ++i)
	{
	    if (this.containerItems[i] == null)
	    {
		return i;
	    }
	}

	return -1;
    }

    // インベントリにアイテムを追加（InventoryPlayer）
    public boolean addItemStackToInventory(ItemStack par1ItemStack)
    {
	this.load();
	int slot;

	if (par1ItemStack == null)
	{
	    this.save();
	    return false;
	} else if (par1ItemStack.stackSize == 0)
	{
	    this.save();
	    return false;
	} else
	{
	    if (par1ItemStack.isItemDamaged())
	    {
		slot = this.getFirstEmptyStack();

		if (slot >= 0)
		{
		    this.containerItems[slot] = ItemStack.copyItemStack(par1ItemStack);
		    par1ItemStack.stackSize = 0;

		    this.save();
		    return true;
		} else
		{
		    this.save();
		    return false;
		}
	    } else
	    {
		do
		{
		    slot = par1ItemStack.stackSize;
		    par1ItemStack.stackSize = this.storePartialItemStack(par1ItemStack);
		} while (par1ItemStack.stackSize > 0 && par1ItemStack.stackSize < slot);

		this.save();
		return par1ItemStack.stackSize < slot;
	    }
	}
    }

    // インベントリにアイテムを格納（1）（InventoryPlayer）
    private int storePartialItemStack(ItemStack par1ItemStack)
    {
	Item item = par1ItemStack.getItem();
	int size = par1ItemStack.stackSize;
	int slot;

	if (par1ItemStack.getMaxStackSize() == 1)
	{
	    slot = this.getFirstEmptyStack();

	    if (slot < 0)
	    {
		return size;
	    } else
	    {
		if (this.containerItems[slot] == null)
		{
		    this.containerItems[slot] = ItemStack.copyItemStack(par1ItemStack);
		}

		return 0;
	    }
	} else
	{
	    slot = this.storeItemStack(par1ItemStack);

	    if (slot < 0)
	    {
		slot = this.getFirstEmptyStack();
	    }

	    if (slot < 0)
	    {
		return size;
	    } else
	    {
		if (this.containerItems[slot] == null)
		{
		    this.containerItems[slot] = new ItemStack(item, 0, par1ItemStack.getItemDamage());

		    if (par1ItemStack.hasTagCompound())
		    {
			this.containerItems[slot].setTagCompound((NBTTagCompound) par1ItemStack.getTagCompound().copy());
		    }
		}

		int i = size;

		if (size > this.containerItems[slot].getMaxStackSize() - this.containerItems[slot].stackSize)
		{
		    i = this.containerItems[slot].getMaxStackSize() - this.containerItems[slot].stackSize;
		}

		if (i > this.getInventoryStackLimit() - this.containerItems[slot].stackSize)
		{
		    i = this.getInventoryStackLimit() - this.containerItems[slot].stackSize;
		}

		if (i == 0)
		{
		    return size;
		} else
		{
		    size -= i;
		    this.containerItems[slot].stackSize += i;

		    return size;
		}
	    }
	}
    }

    // インベントリにアイテムを格納（2）（InventoryPlayer）
    private int storeItemStack(ItemStack par1ItemStack)
    {
	for (int i = 0; i < this.getSizeInventory(); ++i)
	{
	    if (this.containerItems[i] != null && this.containerItems[i].getItem() == par1ItemStack.getItem() && this.containerItems[i].isStackable() && this.containerItems[i].stackSize < this.containerItems[i].getMaxStackSize() && this.containerItems[i].stackSize < this.getInventoryStackLimit() && (!this.containerItems[i].getHasSubtypes() || this.containerItems[i].getItemDamage() == par1ItemStack.getItemDamage()) && ItemStack.areItemStackTagsEqual(this.containerItems[i], par1ItemStack))
	    {
		return i;
	    }
	}

	return -1;
    }

}
