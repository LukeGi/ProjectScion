package com.bluemonster122.projectscion.common.tileentities.farm;

import com.bluemonster122.projectscion.common.blocks.ModBlocks;
import com.bluemonster122.projectscion.common.inventory.InternalDynamicInventory;
import com.bluemonster122.projectscion.common.inventory.InventoryOperation;
import com.bluemonster122.projectscion.common.tileentities.TileEntityInventoryBase;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class TileEntityCobbleGenerator extends TileEntityInventoryBase implements ITickable {

    private int cobbleStored;
    private boolean isFormed;

    public TileEntityCobbleGenerator() {

        setCobbleStored(0);
        setFormed(false);
    }

    @Override
    public void update() {

        if (isFormed() && worldObj.getTotalWorldTime() % 20 == 19) {
            initMachineData();
            if (isFormed() && getCobbleStored() <= 4096) {
                setCobbleStored(getCobbleStored() + 1);
            }
        }
    }

    public void save(NBTTagCompound nbt) {

        nbt.setInteger("storedCobble", getCobbleStored());
        nbt.setBoolean("isFormed", isFormed());
    }

    public void load(NBTTagCompound nbt) {

        setCobbleStored(nbt.getInteger("storedCobble"));
        setFormed(nbt.getBoolean("isFormed"));
    }

    @Override
    public void initMachineData() {

        super.initMachineData();
        setFormed(structureIsCorrect());
    }

    private boolean structureIsCorrect() {

        BlockPos[] positions = new BlockPos[]{pos.north().east(), pos.south().east(), pos.south().west(), pos.north().west()};
        List<Block> blocks = new ArrayList<>(positions.length);
        for (BlockPos pos : positions) {
            blocks.add(worldObj.getBlockState(pos).getBlock());
        }
        if (!blocks.contains(Blocks.stone) || !blocks.contains(Blocks.obsidian) || !blocks.contains(Blocks.netherrack) || !blocks.contains(Blocks.quartz_block)) {
            return false;
        }
        positions = new BlockPos[]{pos.north(), pos.east(), pos.south(), pos.west()};
        for (BlockPos pos : positions) {
            if (worldObj.getBlockState(pos).getBlock() != ModBlocks.COBBLE_GENERATOR.getBlock()) {
                return false;
            }
        }
        return true;
    }

    public int getCobbleStored() {

        return cobbleStored;
    }

    public void setCobbleStored(int cobbleStored) {

        this.cobbleStored = cobbleStored;
    }

    public boolean isFormed() {

        return isFormed;
    }

    public void setFormed(boolean formed) {

        isFormed = formed;
    }

    @Override
    public ItemStackHandler getInternalInventory() {

        ItemStackHandler inv = new ItemStackHandler(1);
        inv.setStackInSlot(0, new ItemStack(Blocks.cobblestone, getCobbleStored()));
        return inv;
    }

    @Override
    public int getInventoryStackLimit() {

        return 2 << 16;
    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InventoryOperation operation, ItemStack removed, ItemStack added) {

    }

    @Override
    public int[] getAccessibleSlotsBySide(EnumFacing side) {

        return new int[]{0};
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {

        return null;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {

        super.writeToNBT(nbtTagCompound);
        save(nbtTagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

        super.readFromNBT(nbtTagCompound);
        load(nbtTagCompound);
    }
}
