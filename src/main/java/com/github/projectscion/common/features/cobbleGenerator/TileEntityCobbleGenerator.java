package com.github.projectscion.common.features.cobbleGenerator;

import com.github.projectscion.common.core.multiblock.TileEntityMultiblock;
import com.github.projectscion.common.features.tools.ItemMiningTool;
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
//TODO: add recipe ( 6 redstone, base gen block and 2 potion of swiftness II ) - Credit to Sharidan
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

    private ItemStackHandler inventory = new ItemStackHandler(1);
    private int counter = 0, speed = 1;

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("inventory", inventory.serializeNBT());
        nbt.setInteger("counter", counter);
        nbt.setInteger("speed", speed);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        inventory.deserializeNBT(nbt.getCompoundTag("inventory"));
        counter = nbt.getInteger("counter");
        speed = nbt.getInteger("speed");
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
        //TODO : Cleanup this function.
        if (getWorld().getTotalWorldTime() % 5 == 0) {
            if (getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN).insertItem(0, new ItemStack(Blocks.COBBLESTONE, speed), true) == null) {
                for (int speeds = 0; speeds < speed; speeds++) {
                    int tool = -1;
                    IItemHandler chest = null;
                    for (int i = 0; i < multiblock.size() && chest == null; i++) {
                        for (EnumFacing f : EnumFacing.VALUES) {
                            BlockPos coord = pos.add(multiblock.get(i).offset(f));
                            TileEntity tile = getWorld().getTileEntity(coord);
                            if (tile == null || tile instanceof TileEntityCobbleGenerator) continue;
                            chest = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                            if (chest != null) break;
                        }
                    }
                    for (int i = 0; i < chest.getSlots(); i++) {
                        ItemStack stack = chest.getStackInSlot(i);
                        if (stack != null && stack.getItem() != null && stack.getItem().canHarvestBlock(Blocks.COBBLESTONE.getDefaultState())) {
                            tool = i;
                            break;
                        }
                    }
                    if (tool != -1) {
                        ItemStack toolItem = chest.extractItem(tool, 1, false);
                        IItemHandler inv = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                        if (ItemHandlerHelper.insertItem(inv, new ItemStack(Blocks.COBBLESTONE, 1), true) == null) {
                            ItemHandlerHelper.insertItem(inv, new ItemStack(Blocks.COBBLESTONE, 1), false);
                            if (toolItem.getItem() instanceof ItemMiningTool) {
                                counter++;
                                if (counter == 16) {
                                    counter = 0;
                                } else {
                                    chest.insertItem(tool, toolItem, false);
                                    return;
                                }
                            }
                        }
                        toolItem.attemptDamageItem(1, worldObj.rand);
                        if (toolItem.getItemDamage() != toolItem.getItem().getMaxDamage()) {
                            chest.insertItem(tool, toolItem, false);
                        }
                    }
                }
            } else {
                for (int i = 0; i < multiblock.size(); i++) {
                    for (EnumFacing f : EnumFacing.VALUES) {
                        BlockPos coord = pos.add(multiblock.get(i).offset(f));
                        TileEntity tile = getWorld().getTileEntity(coord);
                        if (tile == null || tile instanceof TileEntityCobbleGenerator) continue;
                        IItemHandler thatInv = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
                        if (thatInv == null) continue;
                        IItemHandler thisInv = getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                        thisInv.insertItem(0, ItemHandlerHelper.insertItem(thatInv, thisInv.extractItem(0, 64, false), false), false);
                        if (thisInv.getStackInSlot(0) == null) break;
                    }
                }
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
        if (hasMaster() && !isMaster()) {
            return getWorld().getTileEntity(getMasterPos()).getCapability(capability, facing);
        }
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) inventory;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }
}
