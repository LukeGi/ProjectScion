package com.github.projectscion.common.features.cobbleGenerator;

import com.github.projectscion.common.core.multiblock.TileEntityMultiblock;
import com.sun.istack.internal.NotNull;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
//TODO: add recipe ( 6 redstone, base gen block and 2 potion of swiftness II ) - Credit to Sharidan
//TODO: make different.
public class TileEntityCobbleGenerator extends TileEntityMultiblock implements ITickable {

 private static final List<BlockPos> multiblock;

 static {
  multiblock = new ArrayList<>();
  BlockPos pos = new BlockPos(0, 0, 0);
  for (int x = -4; x <= 4; x++) {
   for (int z = -4; z <= 4; z++) {
    multiblock.add(pos.add(x, 0, z));
   }
  }
 }

 private ItemStackHandler inventory = new ItemStackHandler(1);
 private int counter = 0, speed = 1;
 private ItemStack tool;

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
