package com.github.projectscion.common.features.cobbleGenerator;

import com.github.projectscion.common.core.multiblock.TileEntityMultiblock;
import com.github.projectscion.common.util.LogHelper;
import com.sun.istack.internal.NotNull;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class TileEntityCobbleGenerator extends TileEntityMultiblock implements ITickable {

    private static final List<BlockPos> multiblock;

    static {
        multiblock = new ArrayList<>();
        BlockPos pos = new BlockPos(0, 0, 0);
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                multiblock.add(pos.add(x, 0, z));
                multiblock.add(pos.add(x, 2, z));
            }
        }
    }

    private ItemStackHandler inventory = new ItemStackHandler(64);

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("inventory", inventory.serializeNBT());
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
        super.readFromNBT(nbt);
    }

    @Override
    public boolean isSuitable(@NotNull TileEntity tile) {
        return tile instanceof TileEntityCobbleGenerator;
    }

    @Override
    public List<BlockPos> getMultiblock() {
        return multiblock;
    }

    @Override
    public void function() {
        if (getWorld().getTotalWorldTime() % 5 == 0) {
            IItemHandler inv = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
            if (ItemHandlerHelper.insertItem(inv, new ItemStack(Blocks.COBBLESTONE, 1), true) == null) {
                ItemHandlerHelper.insertItem(inv, new ItemStack(Blocks.COBBLESTONE, 1), false);
            }
        }
    }

    @Override
    public boolean checkMuliblockForm() {
        boolean foundFault = false;
        BlockPos lava = new BlockPos(0, 1, 0);
        BlockPos[] cobble = new BlockPos[]{lava.north(), lava.east(), lava.south(), lava.west(), lava.north().east(), lava.south().east(), lava.south().west(), lava.north().west()};
        for (BlockPos blockPos : cobble) {
            if (worldObj.getBlockState(blockPos.add(pos)).getBlock() != Blocks.COBBLESTONE) {
                foundFault = true;
                break;
            }
        }
        if (worldObj.getBlockState(lava.add(pos)).getBlock() != Blocks.FLOWING_LAVA && worldObj.getBlockState(lava.add(pos)).getBlock() != Blocks.LAVA) {
            foundFault = true;
        }
        return super.checkMuliblockForm() && !foundFault;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (hasMaster() && !isMaster()){
            return getWorld().getTileEntity(getMasterPos()).getCapability(capability, facing);
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) inventory;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }
}
