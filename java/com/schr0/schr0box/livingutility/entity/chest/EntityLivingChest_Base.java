package com.schr0.schr0box.livingutility.entity.chest;

import com.schr0.schr0box.livingutility.LivingUtility;
import com.schr0.schr0box.livingutility.entity.base.EntityLivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.inventory.EquipmentLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.inventory.InventoryLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.inventory.SpecialItemsLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.model.ModelMotionLivingChest;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class EntityLivingChest_Base extends EntityLivingUtility
{
    // InventoryLivingChestの宣言
    public InventoryLivingChest inventory = new InventoryLivingChest(this);

    // EquipmentLivingChestの宣言
    public EquipmentLivingChest equipment = new EquipmentLivingChest(this);

    // SpecialItemsLivingChestの宣言
    public SpecialItemsLivingChest specialItems = new SpecialItemsLivingChest(this);

    // ModelMotionLivingChestの宣言
    public ModelMotionLivingChest modelMotion = new ModelMotionLivingChest();

    public EntityLivingChest_Base(World par1World)
    {
	super(par1World);
    }

    // dataWatcherの処理
    // 20 : 開閉状態
    @Override
    protected void entityInit()
    {
	super.entityInit();
	this.getDataWatcher().addObject(20, Byte.valueOf((byte) 0));
    }

    // 開閉の判定 20
    public boolean isOpen()
    {
	return (this.dataWatcher.getWatchableObjectByte(20) & 4) != 0;
    }

    // 開閉の処理 20
    public void setOpen(boolean par1)
    {
	byte b0 = this.dataWatcher.getWatchableObjectByte(20);

	if (par1)
	{
	    this.dataWatcher.updateObject(20, Byte.valueOf((byte) (b0 | 4)));
	} else
	{
	    this.dataWatcher.updateObject(20, Byte.valueOf((byte) (b0 & -5)));
	}
    }

    // NBTの書き込み
    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
	super.writeEntityToNBT(par1NBTTagCompound);

	// 開閉状態
	par1NBTTagCompound.setBoolean("Open", this.isOpen());

	// インベントリの保存
	this.inventory.save();
	this.equipment.save();
	this.specialItems.save();
    }

    // NBTの読み込み
    @Override
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
	super.readEntityFromNBT(par1NBTTagCompound);

	// 開閉状態
	this.setOpen(par1NBTTagCompound.getBoolean("Open"));

	// インベントリの読み込み
	this.inventory.load();
	this.equipment.load();
	this.specialItems.load();
    }

    // （自らが）乗っている場合のアップデート
    @Override
    public void updateRidden()
    {
	super.updateRidden();

	// 何かに乗っている場合
	if (this.isRiding())
	{
	    EntityLivingBase livingbase = (EntityLivingBase) this.ridingEntity;

	    // 乗っている生物と同様の正面を向く
	    this.prevRotationYaw = this.rotationYaw = livingbase.rotationYaw;
	}
    }

    // （自らが）乗っている場合のY座標
    @Override
    public double getYOffset()
    {
	if (this.ridingEntity != null)
	{
	    if (this.ridingEntity instanceof EntityPlayer)
	    {
		return this.yOffset - 1.2F;
	    } else
	    {
		return this.yOffset + 0.15F;
	    }
	}

	return super.getYOffset();
    }

    // 被ダメージ時の音声
    @Override
    protected String getHurtSound()
    {
	return "dig.wood";
    }

    // 死亡時の音声
    @Override
    protected String getDeathSound()
    {
	return "random.break";
    }

    // 足音
    @Override
    protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
    {
	this.playSound("step.wood", 0.25F, 1.0F);
    }

    // インタラクト
    @Override
    public boolean interact(EntityPlayer par1EntityPlayer)
    {
	ItemStack currentItem = par1EntityPlayer.inventory.getCurrentItem();

	// 飼い慣らし状態 ＆ 飼い主である場合
	if (this.isTamed() && this.func_152114_e(par1EntityPlayer))
	{
	    // スニーキング状態の場合
	    if (par1EntityPlayer.isSneaking())
	    {
		// お座りの処理
		this.setSittingEx();
	    } else
	    {
		// Customerのset（独自）
		this.setCustomer(par1EntityPlayer);

		// 独自GUIを表示
		if (!this.worldObj.isRemote)
		{
		    par1EntityPlayer.openGui(LivingUtility.instance, LivingUtility.CHEST_GUI_ID, this.worldObj, (int) this.posX, (int) this.posY, (int) this.posZ);
		}
	    }

	    // Itemを振る動作
	    par1EntityPlayer.swingItem();
	    return true;
	}
	return super.interact(par1EntityPlayer);
    }

    // Entityとしてのアップデート
    @Override
    public void onUpdate()
    {
	super.onUpdate();

	// 開閉モーション
	this.modelMotion.setCoverMotion(this, this.isOpen());
    }

    // 死んだ際にアイテムを落とす処理
    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
	super.dropFewItems(par1, par2);

	float rX = this.rand.nextFloat() * 0.8F + 0.1F;
	float rY = this.rand.nextFloat() * 0.8F + 0.1F;
	float rZ = this.rand.nextFloat() * 0.8F + 0.1F;
	float rand = 0.05F;

	// インベントリの中身をドロップ
	for (int i = 0; i < this.inventory.getSizeInventory(); ++i)
	{
	    ItemStack itemstack = this.inventory.getStackInSlot(i);

	    if (itemstack != null)
	    {
		while (itemstack.stackSize > 0)
		{
		    int j = this.rand.nextInt(21) + 10;

		    if (j > itemstack.stackSize)
		    {
			j = itemstack.stackSize;
		    }

		    itemstack.stackSize -= j;

		    EntityItem entityitem = new EntityItem(this.worldObj, this.posX + rX, this.posY + rY, this.posZ + rZ, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

		    if (itemstack.hasTagCompound())
		    {
			entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
		    }

		    entityitem.motionX = (float) this.rand.nextGaussian() * rand;
		    entityitem.motionY = (float) this.rand.nextGaussian() * rand + 0.2F;
		    entityitem.motionZ = (float) this.rand.nextGaussian() * rand;

		    // クライアントだけの処理
		    if (!this.worldObj.isRemote)
		    {
			this.worldObj.spawnEntityInWorld(entityitem);
		    }
		}
	    }
	}

	// 装備インベントリの中身をドロップ
	for (int i = 0; i < this.equipment.getSizeInventory(); ++i)
	{
	    ItemStack itemstack = this.equipment.getStackInSlot(i);

	    if (itemstack != null)
	    {
		while (itemstack.stackSize > 0)
		{
		    int j = this.rand.nextInt(21) + 10;

		    if (j > itemstack.stackSize)
		    {
			j = itemstack.stackSize;
		    }

		    itemstack.stackSize -= j;

		    EntityItem entityitem = new EntityItem(this.worldObj, this.posX + rX, this.posY + rY, this.posZ + rZ, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

		    if (itemstack.hasTagCompound())
		    {
			entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
		    }

		    entityitem.motionX = (float) this.rand.nextGaussian() * rand;
		    entityitem.motionY = (float) this.rand.nextGaussian() * rand + 0.2F;
		    entityitem.motionZ = (float) this.rand.nextGaussian() * rand;

		    // クライアントだけの処理
		    if (!this.worldObj.isRemote)
		    {
			this.worldObj.spawnEntityInWorld(entityitem);
		    }
		}
	    }
	}

	// スペシャルアイテムの中身をドロップ
	for (int i = 0; i < this.specialItems.getSizeInventory(); ++i)
	{
	    ItemStack itemstack = this.specialItems.getStackInSlot(i);

	    if (itemstack != null)
	    {
		while (itemstack.stackSize > 0)
		{
		    int j = this.rand.nextInt(21) + 10;

		    if (j > itemstack.stackSize)
		    {
			j = itemstack.stackSize;
		    }

		    itemstack.stackSize -= j;

		    EntityItem entityitem = new EntityItem(this.worldObj, this.posX + rX, this.posY + rY, this.posZ + rZ, new ItemStack(itemstack.getItem(), j, itemstack.getItemDamage()));

		    if (itemstack.hasTagCompound())
		    {
			entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
		    }

		    entityitem.motionX = (float) this.rand.nextGaussian() * rand;
		    entityitem.motionY = (float) this.rand.nextGaussian() * rand + 0.2F;
		    entityitem.motionZ = (float) this.rand.nextGaussian() * rand;

		    // クライアントだけの処理
		    if (!this.worldObj.isRemote)
		    {
			this.worldObj.spawnEntityInWorld(entityitem);
		    }
		}
	    }
	}

    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // 現在の体力（GUI用）
    @SideOnly(Side.CLIENT)
    public int getHealthBar()
    {
	return (int) this.getHealth();
    }

    // UtilityCoreによりスポーンした際の処理
    public void onSpawnWithUtilityCore(EntityLivingChest_Base theChest, EntityPlayer player, World world, int pX, int pY, int pZ)
    {
	// 向きの修正
	theChest.setLocationAndAngles(pX + 0.5D, pY, pZ + 0.5D, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
	theChest.rotationYawHead = theChest.rotationYaw;
	theChest.renderYawOffset = theChest.rotationYaw;

	// mobEggから沸いた際に呼ばれる
	theChest.onSpawnWithEgg((IEntityLivingData) null);

	// 飼い慣らし
	theChest.setTamed(true);
	theChest.setPathToEntity((PathEntity) null);
	theChest.setAttackTarget((EntityLivingBase) null);
	theChest.setHealth(20.0F);
	theChest.func_152115_b(player.getUniqueID().toString());
	theChest.worldObj.setEntityState(this, (byte) 7);

	// スポーン
	world.spawnEntityInWorld(theChest);
    }

}
