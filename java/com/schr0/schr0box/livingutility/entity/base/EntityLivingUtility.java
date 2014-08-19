package com.schr0.schr0box.livingutility.entity.base;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public abstract class EntityLivingUtility extends EntityTameable
{
    // プレイヤー
    private EntityPlayer thePlayer;

    public EntityLivingUtility(World par1World)
    {
	super(par1World);
    }

    // 複数落とすアイテム
    @Override
    protected void dropFewItems(boolean par1, int par2)
    {
	super.dropFewItems(par1, par2);

	// 元となったUtility（ItemStack）がある場合にはドロップ
	if (this.getUtility() != null)
	{
	    this.entityDropItem(this.getUtility(), 0.5F);
	}
    }

    // 生まれる赤ん坊の設定
    @Override
    public EntityAgeable createChild(EntityAgeable par1EntityAgeable)
    {
	return null;
    }

    // ------------------------- ↓独自の実装↓ -------------------------//

    // 元となったUtility（ItemStack）のget
    public abstract ItemStack getUtility();

    // 取引をしている間の判定
    public boolean isTrading()
    {
	return this.thePlayer != null;
    }

    // Customerのget
    public EntityPlayer getCustomer()
    {
	return this.thePlayer;
    }

    // Customerのset
    public void setCustomer(EntityPlayer par1EntityPlayer)
    {
	this.thePlayer = par1EntityPlayer;
    }

    // メッセージの出力
    public void information(String message)
    {
	// オーナー（EntityPlayer）が存在している場合
	if (this.getOwner() != null && this.getOwner() instanceof EntityPlayer && !this.worldObj.isRemote)
	{
	    // メッセージを表示
	    ((EntityPlayer) this.getOwner()).addChatComponentMessage(new ChatComponentTranslation(message, new Object[0]));
	}
    }

    // SEの出力
    public void playSE(String type, float vol, float pitch)
    {
	this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ, type, vol, pitch);
    }

    // お座りの処理
    public void setSittingEx()
    {
	// クライアントだけの処理
	if (!this.worldObj.isRemote)
	{
	    String name = this.getCommandSenderName();

	    // お座り状態
	    if (this.isSitting())
	    {
		// メッセージの出力（独自）
		this.information(name + " : Stand");
	    }
	    // お座り状態でない
	    else
	    {
		// メッセージの出力（独自）
		this.information(name + " : Sit");
	    }

	    this.aiSit.setSitting(!this.isSitting());
	    this.isJumping = false;
	    this.setPathToEntity((PathEntity) null);
	    this.setTarget((Entity) null);
	    this.setAttackTarget((EntityLivingBase) null);
	}

	// 音を出す
	this.playSE("random.click", 1.0F, 1.0F);
    }

}
