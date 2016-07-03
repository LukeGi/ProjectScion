package com.github.projectscion.common.util;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

public class InventoryHelper {

 public static ItemStack addItemStackToInventory(ItemStack itemIn, IInventory inventory, int slotStart,
                                                 int slotEnd) {

  return addItemStackToInventory(itemIn, inventory, slotStart, slotEnd, false);
 }

 public static boolean breakBlockIntoPlayerInv(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {

  IBlockState state = world.getBlockState(pos);
  if (stack.getItem().getStrVsBlock(stack, state) != 0) {
   List<ItemStack> drops = state.getBlock().getDrops(world, pos, state, 0);
   world.destroyBlock(pos, false);
   if (!world.isRemote) {
    for (ItemStack drop : drops) {
     ItemHandlerHelper.giveItemToPlayer(player, drop);
    }
   }
   return true;
  } else {
   return false;
  }
 }

 public static ItemStack addItemStackToInventory(ItemStack itemIn, IInventory inventory, int slotStart, int slotEnd,
                                                 boolean simulate) {

  if (itemIn == null)
   return null;
  ItemStack itemOut = itemIn.copy();
  for (int i = slotStart; i <= slotEnd; i++) {
   ItemStack slotItemStack = inventory.getStackInSlot(i) == null ? null : inventory.getStackInSlot(i).copy();
   if (itemOut == null)
    return null;
   if (slotItemStack == null) {
    if (!simulate)
     inventory.setInventorySlotContents(i, itemOut);
    return null;
   }
   if (!(ItemStack.areItemsEqual(itemOut, slotItemStack)))
    continue;
   if (slotItemStack.stackSize == slotItemStack.getMaxStackSize())
    continue;
   if (itemOut.stackSize + slotItemStack.stackSize >= slotItemStack.getMaxStackSize()) {
    int sizeRemaining = slotItemStack.getMaxStackSize() - slotItemStack.stackSize;
    itemOut.stackSize = itemOut.stackSize - sizeRemaining;
    if (!simulate)
     slotItemStack.stackSize = slotItemStack.stackSize + sizeRemaining;
    if (!simulate)
     inventory.setInventorySlotContents(i, slotItemStack);
    if (itemOut.stackSize == 0)
     itemOut = null;
    continue;
   }
   slotItemStack.stackSize = slotItemStack.stackSize + itemOut.stackSize;
   itemOut = null;
   if (!simulate)
    inventory.setInventorySlotContents(i, slotItemStack);
   break;
  }
  if (itemOut != null)
   return itemOut;
  return null;
 }
}
