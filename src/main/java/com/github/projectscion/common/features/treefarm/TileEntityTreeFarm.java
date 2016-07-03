package com.github.projectscion.common.features.treefarm;

import com.github.projectscion.common.core.multiblock.TileEntityMultiblock;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.sun.istack.internal.NotNull;
import gnu.trove.set.hash.THashSet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class TileEntityTreeFarm extends TileEntityMultiblock {

 public static final List<BlockPos> multiblock;

 static {
  multiblock = new ArrayList<>();
  BlockPos pos = new BlockPos(0, 0, 0);
  for (int i = -2; i <= 2; i++) {
   multiblock.add(pos.add(i, 0, 0));
   if (i != 0) {
    multiblock.add(pos.add(0, 0, i));
   }
  }
 }

 private ItemStackHandler internalInventory = new ItemStackHandler(100);
 private List<BlockPos> toChop = new ArrayList<>();
 private TreeFarmOperation operation = TreeFarmOperation.NONE;
 private int scanx, scany = 0, scanz;

 private static boolean isLog(World world, BlockPos pos) {
  return world.getBlockState(pos).getBlock().isWood(world, pos) || world.getBlockState(pos).getBlock().isLeaves(world.getBlockState(pos), world, pos);
 }

 public void save(NBTTagCompound nbt) {

  nbt.setInteger("scanx", scanx);
  nbt.setInteger("scany", scany);
  nbt.setInteger("scanz", scanz);

  nbt.setTag("inventory", internalInventory.serializeNBT());
 }

 public void load(NBTTagCompound nbt) {
  scanx = nbt.getInteger("scanx");
  scany = nbt.getInteger("scany");
  scanz = nbt.getInteger("scanz");
  internalInventory.deserializeNBT(nbt.getCompoundTag("inventory"));
 }

 @Override
 public void function() {
  if (worldObj.getTotalWorldTime() % 100 == 0) {
   pickupDrops();
  }
  if (operation.equals(TreeFarmOperation.NONE)) {
   operation = TreeFarmOperation.SCANNING;
  }
  if (operation.equals(TreeFarmOperation.CUTTING)) {
   operation.equals(TreeFarmOperation.SCANNING);
   return;
  }
  if (operation.equals(TreeFarmOperation.SCANNING) || operation.equals(TreeFarmOperation.PLANTING)) {
   moveScanner();
   BlockPos blockPos = pos.add(scanx, 1, scanz);
   if (operation.equals(TreeFarmOperation.SCANNING)) {
    MinecraftForge.EVENT_BUS.register(new TreeChoppingTask(blockPos, this, 8));
   }
   if (operation.equals(TreeFarmOperation.PLANTING)) {
    plant(blockPos);
   }
  }
 }

 private void plant(BlockPos blockPos) {
  Block block = worldObj.getBlockState(blockPos).getBlock();
  IItemHandler output = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
  if (block.isReplaceable(worldObj, blockPos)) {
   for (int i = 0; i < output.getSlots(); i++) {
    ItemStack stack = output.getStackInSlot(i);
    if (stack != null && stack.getItem() instanceof ItemBlock) {
     block = ((ItemBlock) stack.getItem()).getBlock();
     if (block.getUnlocalizedName().toLowerCase().contains("sapling") && ((BlockSapling) block).canBlockStay(worldObj, blockPos, block.getStateFromMeta(stack.getItemDamage()))) {
      worldObj.setBlockState(blockPos, block.getStateFromMeta(stack.getItemDamage()), 3);
      output.extractItem(i, 1, false);
      break;
     }
    }
   }
  }
 }

 private void moveScanner() {
  int radius = 2;
  scanx++;
  if (scanx == radius + 1) {
   scanz++;
   scanx = -radius;
   if (scanz == radius + 1) {
    scanz = -radius;
    if (getOperation().equals(TreeFarmOperation.PLANTING)) {
     setOperation(TreeFarmOperation.SCANNING);
    } else if (getOperation().equals(TreeFarmOperation.SCANNING)) {
     setOperation(TreeFarmOperation.PLANTING);
    }
   }
  }
 }

 private void pickupDrops() {
  AxisAlignedBB aabb = getPickupAABB();
  if (aabb != null) {
   List<ItemStack> acceptedDrops = new ArrayList<>();
   for (ItemStack itemStack : OreDictionary.getOres("treeSapling")) {
    acceptedDrops.add(itemStack);
   }
   for (ItemStack itemStack : OreDictionary.getOres("logWood")) {
    acceptedDrops.add(itemStack);
   }
   List<EntityItem> loots = worldObj.getEntitiesWithinAABB(EntityItem.class, aabb);
   EntityItem item;
   for (int i = loots.size() - 1; i >= 0; i--) {
    item = loots.get(i);
    boolean remove = true;
    for (ItemStack acceptedDrop : acceptedDrops) {
     if (item.getEntityItem().getItem() == acceptedDrop.getItem()) {
      remove = false;
     }
    }
    if (remove) {
     loots.remove(i);
    }
   }
   for (EntityItem loot : loots) {
    ItemHandlerHelper.insertItem(internalInventory, loot.getEntityItem(), false);
    loot.setDead();
   }
  }
 }

 private void damageTool() {
  List<IItemHandler> possibleIOs = new ArrayList<>();
  for (int i = 0; i < multiblock.size(); i++) {
   BlockPos coord = multiblock.get(i).up(1);
   TileEntity tile = getWorld().getTileEntity(coord);
   IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

  }
 }

 @Override
 public boolean isSuitable(@NotNull TileEntity tile) {
  return tile instanceof TileEntityTreeFarm;
 }

 @Override
 public List<BlockPos> getMultiblock() {
  return multiblock;
 }

 @Override
 public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {

  save(nbtTagCompound);
  return super.writeToNBT(nbtTagCompound);
 }

 @Override
 public void readFromNBT(NBTTagCompound nbtTagCompound) {

  load(nbtTagCompound);
  super.readFromNBT(nbtTagCompound);
 }

 public AxisAlignedBB getPickupAABB() {
  return new AxisAlignedBB(pos.getX() - 10, pos.getY() - 1, pos.getZ() - 10, pos.getX() + 10, pos.getY() + 3, pos.getZ() + 10);
 }

 @Override
 public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
  return capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) || super.hasCapability(capability, facing);
 }

 @Override
 public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
  if (hasMaster() && !isMaster()) {
   return getWorld().getTileEntity(getMasterPos()).getCapability(capability, facing);
  }
  if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
   return (T) internalInventory;
  }
  return super.getCapability(capability, facing);
 }

 public EntityPlayer getFakePlayer() {
  return FakePlayerFactory.get(worldObj.getMinecraftServer().worldServerForDimension(worldObj.provider.getDimension()), new GameProfile(UUID.randomUUID(), "treefarm"));
 }

 public TreeFarmOperation getOperation() {
  return operation;
 }

 public void setOperation(TreeFarmOperation operation) {
  this.operation = operation;
 }

 private boolean canWork() {
  return ItemHandlerHelper.insertItem(getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN), new ItemStack(Blocks.LOG, 1), true) == null;
 }

 public static class TreeChoppingTask {

  public final World world;
  public final TileEntityTreeFarm tile;
  public final int blocksPerTick;

  public Queue<BlockPos> blocks = Lists.newLinkedList();
  public Set<BlockPos> visited = new THashSet<>();

  public TreeChoppingTask(BlockPos start, TileEntityTreeFarm tile, int blocksPerTick) {
   this.world = tile.worldObj;
   this.tile = tile;
   this.blocksPerTick = blocksPerTick;

   this.blocks.add(start);
//            this.tile.setOperation(TreeFarmOperation.CUTTING);
  }

  @SubscribeEvent
  public void chopTask(TickEvent.WorldTickEvent event) {
   if (event.side.isClient()) {
    cleanUp();
    return;
   }
   if (!tile.canWork()) {
    return;
   }
   int blocksToGo = blocksPerTick;
   BlockPos pos;
   while (blocksToGo > 0) {
    if (blocks.isEmpty()) {
     cleanUp();
     return;
    }

    pos = blocks.remove();
    if (!visited.add(pos)) {
     continue;
    }

    if (!isLog(world, pos)) {
     continue;
    }

    for (EnumFacing facing : new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST}) {
     BlockPos pos1 = pos.offset(facing);
     if (!visited.contains(pos1)) {
      blocks.add(pos1);
     }
    }

    for (int x = 0; x < 3; x++) {
     for (int z = 0; z < 3; z++) {
      BlockPos pos2 = pos.add(-1 + x, 1, -1 + z);
      if (!visited.contains(pos2)) {
       blocks.add(pos2);
      }
     }
    }

    if (world.isAirBlock(pos)) {
     continue;
    }
    IBlockState state = world.getBlockState(pos);
    Block block = state.getBlock();

    if (!world.isRemote) {
     List<ItemStack> drops;
     drops = block.getDrops(world, pos, world.getBlockState(pos), 0);

     for (int i = 0; i < drops.size(); i++) {
      ItemStack drop = drops.get(i);
      EntityItem item = new EntityItem(world, tile.pos.getX() + 0.5, tile.pos.getY() + 3, tile.pos.getZ() + 0.5, drop);
      item.setPickupDelay(200);
      item.setThrower(this.toString());
      item.setVelocity(0, 0, 0);
      world.spawnEntityInWorld(item);
     }

     world.setBlockToAir(pos);
    }

    blocksToGo--;
   }
   tile.pickupDrops();
  }

  public void cleanUp() {
   MinecraftForge.EVENT_BUS.unregister(this);
  }
 }
}