package com.bluemonster122.projectscion.common.tileentities;

import com.bluemonster122.projectscion.common.inventory.InventoryOperation;
import com.bluemonster122.projectscion.common.inventory.IInventoryCustom;
import com.bluemonster122.projectscion.common.inventory.IInventoryHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public abstract class TileEntityInventoryBase extends TileEntityBase implements ISidedInventory, IInventoryHandler {
    public abstract ItemStackHandler getInternalInventory();

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);

        if (getInternalInventory() != null) {
            getInternalInventory().deserializeNBT(nbtTagCompound.getCompoundTag("inventory"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);

        if (getInternalInventory() != null) {
            nbtTagCompound.setTag("inventory", getInternalInventory().serializeNBT());
        }

    }

    @Override
    public int getSizeInventory() {
        return getInternalInventory().getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return getInternalInventory().getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int i, int j) {
        return getInternalInventory().extractItem(i, j,false);
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemStack) {
        getInternalInventory().setStackInSlot(slot, itemStack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        final double squaredMCReach = 64.0D;
        return this.worldObj.getTileEntity(this.pos) == this && player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.getPos().getZ() + 0.5D) <= squaredMCReach;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        return true;
    }

    @Override
    public abstract void onChangeInventory(IInventory inv, int slot, InventoryOperation operation, ItemStack removed, ItemStack added);

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return this.getAccessibleSlotsBySide(side);
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        return this.isItemValidForSlot(index, itemStackIn);
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        return true;
    }

    public abstract int[] getAccessibleSlotsBySide(EnumFacing side);

    @Override
    public String getName() {
        return getCustomName();
    }

    @Override
    public void saveChanges() {

    }


    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

        return capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

        return capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? (T) new SidedInvWrapper(this, facing) : super.getCapability(capability, facing);
    }
}
