package com.schr0.schr0box.livingutility.entity.chest.inventory.container;

import com.schr0.schr0box.livingutility.LivingUtility;
import com.schr0.schr0box.livingutility.entity.chest.EntityLivingChestEnder_Collect;
import com.schr0.schr0box.livingutility.entity.chest.inventory.EquipmentLivingChest;
import com.schr0.schr0box.livingutility.entity.chest.inventory.SpecialItemsLivingChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryEnderChest;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by A.K. on 14/08/22.
 */
public class ContainerInventoryLivingChestEnder_Collect extends Container {
    private EntityLivingChestEnder_Collect theCollectChest;
    private InventoryEnderChest theCollectChestInventory;
    private EquipmentLivingChest theCollectChestEquipment;
    private SpecialItemsLivingChest theCollectChestSpecialItems;

    public ContainerInventoryLivingChestEnder_Collect(InventoryPlayer inventoryPlayer, EntityLivingChestEnder_Collect livingCollectChest)
    {
        int numRows = livingCollectChest.inventory.getSizeInventory() / 9;
        int slotX = 8;
        int slotY = 0;

        this.theCollectChest = livingCollectChest;
        this.theCollectChestInventory = (livingCollectChest.getOwner() instanceof EntityPlayer)? ((EntityPlayer)livingCollectChest.getOwner()).getInventoryEnderChest():null;
        this.theCollectChestEquipment = livingCollectChest.equipment;
        this.theCollectChestSpecialItems = livingCollectChest.specialItems;

        livingCollectChest.inventory.load();
        livingCollectChest.equipment.load();
        livingCollectChest.specialItems.load();

        // スロットを設置
        // addSlotToContainer( Slot(IInventory, slotIndex,xDisplayPosition, yDisplayPosition) )

        // 手持ちのアイテム
        this.addSlotToContainer(new Slot(livingCollectChest.equipment, 0, 109, 46)
        {
            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
        });
        // 頭装備
        this.addSlotToContainer(new Slot(theCollectChest.equipment, 1, 8, 17)
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
        this.addSlotToContainer(new Slot(theCollectChest.equipment, 2, 8, 53)
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
        this.addSlotToContainer(new Slot(theCollectChest.equipment, 3, 81, 17)
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
        this.addSlotToContainer(new Slot(theCollectChest.equipment, 4, 81, 53)
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

        // 座標地点登録用
        this.addSlotToContainer(new Slot(livingCollectChest.specialItems, 0, 145, 47)
        {
            @Override
            public boolean isItemValid(ItemStack p_75214_1_)
            {
                return super.isItemValid(p_75214_1_) && !this.getHasStack() && p_75214_1_.getItem() == LivingUtility.item_HomePointTicket;
            }

            @Override
            public int getSlotStackLimit()
            {
                return 1;
            }
        });

        // LivingChestインベントリ（3 * 9）
        for (int slotCol = 0; slotCol < numRows; ++slotCol)
        {
            for (int slotRow = 0; slotRow < 9; ++slotRow)
            {
                slotY = 75;
                this.addSlotToContainer(new Slot(livingCollectChest.inventory, (slotRow + slotCol * 9), (slotX + slotRow * 18), (slotY + slotCol * 18)));
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
        return this.theCollectChestInventory != null && this.theCollectChestInventory.isUseableByPlayer(entityplayer) && this.theCollectChest.isEntityAlive() && this.theCollectChest.getDistanceToEntity(entityplayer) < 8.0F;
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

            int minSlotSize = 6;
            int maxSlotSize = this.theCollectChestInventory.getSizeInventory() + minSlotSize;

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
        if (this.theCollectChestInventory != null) {
            this.theCollectChestInventory.markDirty();
        }
        this.theCollectChestEquipment.save();
        this.theCollectChestSpecialItems.save();
    }
}
