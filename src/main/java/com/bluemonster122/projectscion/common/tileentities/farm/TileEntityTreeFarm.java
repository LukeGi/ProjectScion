package com.bluemonster122.projectscion.common.tileentities.farm;

import com.bluemonster122.projectscion.common.inventory.InternalInventory;
import com.bluemonster122.projectscion.common.inventory.InventoryOperation;
import com.bluemonster122.projectscion.common.tileentities.TileEntityAreaDefinition;
import com.bluemonster122.projectscion.common.tileentities.TileEntityInventoryBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class TileEntityTreeFarm extends TileEntityFarm {

    private InternalInventory internalInventory = new InternalInventory(this, 100);

    @Override
    public IInventory getInternalInventory() {

        return internalInventory;
    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InventoryOperation operation, ItemStack removed, ItemStack added) {

    }

    @Override
    public int[] getAccessibleSlotsBySide(EnumFacing side) {

        return new int[0];
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        return null;
    }
}
