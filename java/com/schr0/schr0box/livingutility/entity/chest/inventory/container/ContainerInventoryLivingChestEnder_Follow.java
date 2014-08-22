package com.schr0.schr0box.livingutility.entity.chest.inventory.container;

import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChestEnder_Follow;
import com.schr0.schr0box.livingutility.entity.chest.inventory.EquipmentLivingChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by A.K. on 14/08/22.
 */
public class ContainerInventoryLivingChestEnder_Follow extends Container {
    private EntityLivingChestEnder_Follow theFollowChest;
    private InventoryEnderChest theFollowChestInventory;
    private EquipmentLivingChest theFollowChestEquipment;

    public ContainerInventoryLivingChestEnder_Follow(InventoryPlayer inventoryPlayer, EntityLivingChestEnder_Follow livingFollowChest)
    {
        int numRows = livingFollowChest.inventory.getSizeInventory() / 9;
        int slotX = 8;
        int slotY = 0;

        this.theFollowChest = livingFollowChest;
        this.theFollowChestInventory =  (livingFollowChest.getOwner() instanceof EntityPlayer)? ((EntityPlayer)livingFollowChest.getOwner()).getInventoryEnderChest():null;
        this.theFollowChestEquipment = livingFollowChest.equipment;

//        livingFollowChest.inventory.load();
        livingFollowChest.equipment.load();

        // スロットを設置
        // addSlotToContainer( Slot(IInventory, slotIndex,xDisplayPosition, yDisplayPosition) )

        // 手持ちのアイテム
        this.addSlotToContainer(new Slot(theFollowChest.equipment, 0, 127, 46)
        {
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
        });

        // 頭装備
        this.addSlotToContainer(new Slot(theFollowChest.equipment, 1, 8, 17)
        {
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }

            @Override
            public boolean isItemValid(ItemStack itemStack)
            {
                if (itemStack == null)
                    return false;
                return itemStack.getItem().isValidArmor(itemStack, 0, null);
            }

        });

        // 手装備
        this.addSlotToContainer(new Slot(theFollowChest.equipment, 2, 8, 53)
        {
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }

            @Override
            public boolean isItemValid(ItemStack itemStack)
            {
                if (itemStack == null)
                    return false;
                return itemStack.getItem().isValidArmor(itemStack, 1, null);
            }

        });

        // 足装備
        this.addSlotToContainer(new Slot(theFollowChest.equipment, 3, 81, 17)
        {
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }

            @Override
            public boolean isItemValid(ItemStack itemStack)
            {
                if (itemStack == null)
                    return false;
                return itemStack.getItem().isValidArmor(itemStack, 2, null);
            }

        });

        // 靴装備
        this.addSlotToContainer(new Slot(theFollowChest.equipment, 4, 81, 53)
        {
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }

            @Override
            public boolean isItemValid(ItemStack itemStack)
            {
                if (itemStack == null)
                    return false;
                return itemStack.getItem().isValidArmor(itemStack, 3, null);
            }

        });

        // LivingChestインベントリ（3 * 9）
        for (int slotCol = 0; slotCol < numRows; ++slotCol)
        {
            for (int slotRow = 0; slotRow < 9; ++slotRow)
            {
                slotY = 75;
                this.addSlotToContainer(new Slot(theFollowChestInventory, (slotRow + slotCol * 9), (slotX + slotRow * 18), (slotY + slotCol * 18)));
            }
        }

        // プレイヤーインベントリ上部（3 * 9）
        for (int slotCol = 0; slotCol < 3; ++slotCol)
        {
            for (int slotRow = 0; slotRow < 9; ++slotRow)
            {
                slotY = 142;

                this.addSlotToContainer(new Slot(inventoryPlayer, (slotRow + slotCol * 9 + 9), (slotX + slotRow * 18), (slotY + slotCol * 18)));
            }
        }

        // プレイヤーインベントリ下部（1 * 9）
        for (int slotRow = 0; slotRow < 9; ++slotRow)
        {
            slotY = 199;

            this.addSlotToContainer(new Slot(inventoryPlayer, slotRow, slotX + slotRow * 18, slotY));
        }
    }

    // 開けていられるかどうかの判定
    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return this.theFollowChestInventory != null && this.theFollowChestInventory.isUseableByPlayer(entityplayer) && this.theFollowChest.isEntityAlive() && this.theFollowChest.getDistanceToEntity(entityplayer) < 8.0F;
    }

    // Shift+左クリックしたときの処理
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotIndex)
    {
        Slot slot = (Slot) this.inventorySlots.get(slotIndex);
        ItemStack srcItemStack = null;

        // Shift+左クリックしたスロットに何かしらのアイテムがあった場合.
        if (slot != null && slot.getHasStack())
        {
            // スロットからItemStackを取り出し, 移動先のdestItemStackに入れておく.
            // mergeItemStack(移動するItemStack, 移動先の最小スロット番号, 移動先の最大スロット番号, 昇順or降順)

            ItemStack destItemStack = slot.getStack();
            srcItemStack = destItemStack.copy();

            int minSlotSize = 5;
            int maxSlotSize = this.theFollowChestInventory.getSizeInventory() + minSlotSize;

            // 上インベントリ内なら, 下のインベントリに移動.
            if (slotIndex < maxSlotSize && !this.mergeItemStack(destItemStack, maxSlotSize, this.inventorySlots.size(), false))
            {
                return null;
            }

            // 下の1*9インベントリ内なら, 上のインベントリに移動
            if (slotIndex >= maxSlotSize && !this.mergeItemStack(destItemStack, minSlotSize, maxSlotSize, false))
            {
                return null;
            }

            // 移動後にスタック数がゼロならスロットを空にする.
            if (destItemStack.stackSize == 0)
            {
                slot.putStack((ItemStack) null);
            } else
            {
                // スロット更新通知
                slot.onSlotChanged();
            }
        }

        return srcItemStack;
    }

    @Override
    public void onContainerClosed(EntityPlayer p_75134_1_)
    {
        super.onContainerClosed(p_75134_1_);
        if (this.theFollowChestInventory != null) {
            this.theFollowChestInventory.markDirty();
        }
        this.theFollowChestEquipment.save();
    }
}
