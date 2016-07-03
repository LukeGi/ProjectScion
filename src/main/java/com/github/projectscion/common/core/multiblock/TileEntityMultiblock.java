package com.github.projectscion.common.core.multiblock;

import com.sun.istack.internal.NotNull;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public abstract class TileEntityMultiblock extends TileEntity implements ITickable {

 private boolean isMaster, hasMaster;
 private int masterX, masterY, masterZ;

 @Override
 public void update() {
  if (!getWorld().isRemote) {
   if (hasMaster()) {
    if (isMaster()) {
     function();
    }
   } else {
    if (checkMuliblockForm()) {
     setupStructure();
    }
   }
  }
 }

 public boolean checkMuliblockForm() {
  boolean foundFault = false;

  List<BlockPos> multiblock = getMultiblock();
  for (BlockPos relativeScanPos : multiblock) {
   BlockPos scanPos = relativeScanPos.add(getPos());
   TileEntity tile = getWorld().getTileEntity(scanPos);
   if (tile != null && isSuitable(tile)) {
    if (this.isMaster()) {
     if (!((TileEntityMultiblock) tile).hasMaster()) {
      foundFault = true;
      break;
     }
    } else if (((TileEntityMultiblock) tile).hasMaster()) {
     foundFault = true;
     break;
    }
   } else {
    foundFault = true;
   }
  }
  return !foundFault;
 }

 public void setupStructure() {
  List<BlockPos> multiblock = getMultiblock();
  for (BlockPos relativeScanPos : multiblock) {
   BlockPos scanPos = relativeScanPos.add(getPos());
   TileEntity tile = worldObj.getTileEntity(scanPos);
   boolean master = (scanPos.getX() == getPos().getX() && scanPos.getY() == getPos().getY() && scanPos.getZ() == getPos().getZ());
   if (tile != null && (tile instanceof TileEntityMultiblock)) {
    ((TileEntityMultiblock) tile).setMasterPos(getPos());
    ((TileEntityMultiblock) tile).setHasMaster(true);
    ((TileEntityMultiblock) tile).setMaster(master);
   }
  }
 }

 public void reset() {
  masterX = 0;
  masterY = 0;
  masterZ = 0;
  hasMaster = false;
  isMaster = false;
 }

 public boolean checkForMaster() {
  TileEntity tile = worldObj.getTileEntity(new BlockPos(masterX, masterY, masterZ));
  return (tile != null && isSuitable(tile));
 }

 public void resetStructure() {
  List<BlockPos> multiblock = getMultiblock();
  for (BlockPos relativeScanPos : multiblock) {
   BlockPos scanPos = relativeScanPos.add(getPos());
   TileEntity tile = worldObj.getTileEntity(scanPos);
   if (tile != null && isSuitable(tile)) {
    ((TileEntityMultiblock) tile).reset();
   }
  }
 }

 /**
  * This method should return true if tile instanceof Class.
  *
  * @param tile - the tile to be judged.
  * @return tile instanceof Class
  */
 public abstract boolean isSuitable(@NotNull TileEntity tile);

 public abstract List<BlockPos> getMultiblock();

 public abstract void function();

 @Override
 public NBTTagCompound writeToNBT(NBTTagCompound compound) {

  return super.writeToNBT(compound);
 }

 @Override
 public void readFromNBT(NBTTagCompound compound) {
  super.readFromNBT(compound);
 }

 public boolean isMaster() {
  return isMaster;
 }

 public void setMaster(boolean master) {
  isMaster = master;
 }

 public boolean hasMaster() {
  return hasMaster;
 }

 public void setHasMaster(boolean hasMaster) {
  this.hasMaster = hasMaster;
 }

 public BlockPos getMasterPos() {
  return new BlockPos(masterX, masterY, masterZ);
 }

 public void setMasterPos(BlockPos master) {
  masterX = master.getX();
  masterY = master.getY();
  masterZ = master.getZ();
 }
}
