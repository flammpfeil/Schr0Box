package com.schr0.schr0box.livingutility.entity.chest.ai.normal;

import net.minecraft.inventory.IInventory;

import com.schr0.schr0box.livingutility.core.InventoryLib;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.ai.AIBaseEntityLivingChest;

public class EntityLivingChestAITransportInv_Normal extends AIBaseEntityLivingChest
{
    private float moveSpeed;
    private int canInsertRange;

    private IInventory myInventory;
    private IInventory targetInventory;

    public EntityLivingChestAITransportInv_Normal(EntityLivingChest basechest, float speed, int collectrange)
    {
	super(basechest);
	this.setMutexBits(3);

	this.moveSpeed = speed;
	this.canInsertRange = collectrange;
    }

    // AIが始まる判定
    @Override
    public boolean shouldExecute()
    {
	// ターゲットの初期化
	this.targetInventory = null;

	// 登録インベントリ
	IInventory home = this.homePoint.getNearHomeInventory(this.baseChest.posX, this.baseChest.posY, this.baseChest.posZ, 8, 1, 8);

	// 登録インベントリがある場合
	if (home != null)
	{
	    // 搬入可能なインベントリのget
	    this.targetInventory = InventoryLib.getCanInsertInventory(this.baseChest.inventory, home, this.homePoint.getHomeSide());
	}

	// ターゲットが登録されている場合
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
	// ターゲットが登録されていない場合
	if (this.targetInventory == null)
	{
	    return false;
	}

	// tickCounterが60以上になる場合
	if (60 < this.getTickCounter())
	{
	    // ----------------- ログ ----------------- //
	    System.out.println(this.getClass().getName() + " !! ORVER !! ");
	    return false;
	}

	return true;
    }

    // AIが終了する際に呼ばれる処理
    @Override
    public void resetTask()
    {
	super.resetTask();

	// ターゲットの初期化
	this.targetInventory = null;

	// 蓋を閉じる
	this.baseChest.setOpen(false);
    }

    // AIの処理
    @Override
    public void updateTask()
    {
	super.updateTask();

	// 座標の誤差修正
	double bx = ((double) (float) this.homePoint.getHomePosX()) + 0.5D;
	double by = (double) this.homePoint.getHomePosY();
	double bz = ((double) (float) this.homePoint.getHomePosZ()) + 0.5D;

	// ターゲットと0.5D以上離れている場合
	if (0.5D < this.baseChest.getDistanceSq(bx, by, bz))
	{
	    // pathNavigaterがない場合
	    if (this.pathNavigater.noPath())
	    {
		// ターゲットを注視
		this.baseChest.getLookHelper().setLookPosition(bx, by, bz, 10.0F, this.baseChest.getVerticalFaceSpeed());

		// ターゲットに近づく
		this.pathNavigater.tryMoveToXYZ(bx, by, bz, this.moveSpeed);
	    }

	    // 周囲のホームインベントリを取得（canInsertRange, canInsertRange/2, canInsertRange）
	    IInventory nearinventory = this.homePoint.getNearHomeInventory(this.baseChest.posX, this.baseChest.posY, this.baseChest.posZ, this.canInsertRange, this.canInsertRange / 2, this.canInsertRange);

	    // 周囲に搬入可能なホームインベントリがある場合
	    if (nearinventory != null)
	    {
		// インベントリ -> インベントリの搬入
		InventoryLib.insertInvToInv(this.baseChest.inventory, nearinventory, this.homePoint.getHomeSide());

		// ターゲットの初期化
		this.targetInventory = null;
	    }
	}

	// 蓋の開閉処理
	this.baseChest.modelMotion.setCoverMotion(this.baseChest, this.targetInventory == null);
    }
}
