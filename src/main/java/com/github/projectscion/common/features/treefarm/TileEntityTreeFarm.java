package com.github.projectscion.common.features.treefarm;

import com.github.projectscion.common.core.multiblock.TileEntityMultiblock;
import com.github.projectscion.common.features.tools.ItemChainsaw;
import com.github.projectscion.common.util.LogHelper;
import com.sun.istack.internal.NotNull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private ItemStackHandler internalInventory = new ItemStackHandler(27);
    private List<BlockPos> toChop = new ArrayList<>();
    private TreeFarmOperation operation = TreeFarmOperation.NONE;
    private int scanx, scany = 0, scanz;

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
        LogHelper.info(operation.toString());
        if (!toChop.isEmpty()) {
            operation = TreeFarmOperation.CUTTING;
        }
        if (operation.equals(TreeFarmOperation.NONE)) {
            operation = TreeFarmOperation.SCANNING;
        }

        if (operation.equals(TreeFarmOperation.CUTTING)) {
            BlockPos blockPos = toChop.get(0);
            toChop.remove(0);
            worldObj.destroyBlock(blockPos, true);
            if (toChop.isEmpty()) {
                operation = TreeFarmOperation.SCANNING;
            }
            return;
        }
        if (operation.equals(TreeFarmOperation.SCANNING) || operation.equals(TreeFarmOperation.PLANTING)) {
            scanx++;
            if (scanx == 3) {
                scanz++;
                scanx = -2;
                if (scanz == 3) {
                    scanz = -2;
                    if (operation.equals(TreeFarmOperation.SCANNING)) {
                        // Pickup Drops
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
                                ItemStack stack = ItemHandlerHelper.insertItem(internalInventory, loot.getEntityItem(), false);
                                loot.setDead();
                                if (stack != null) {
                                    //TODO make the thing not work
                                    Random rand = new Random();
                                    float dX = rand.nextFloat() * 0.8F + 0.1F;
                                    float dY = rand.nextFloat() * 0.8F + 0.1F;
                                    float dZ = rand.nextFloat() * 0.8F + 0.1F;
                                    EntityItem entityItem = new EntityItem(getWorld(), pos.getX() + dX, pos.getY() + dY, pos.getZ() + dZ, stack.copy());
                                    if (stack.hasTagCompound()) {
                                        entityItem.getEntityItem().setTagCompound(stack.getTagCompound().copy());
                                    }
                                    float factor = 0.05F;
                                    entityItem.motionX = rand.nextGaussian() * factor;
                                    entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
                                    entityItem.motionZ = rand.nextGaussian() * factor;
                                    getWorld().spawnEntityInWorld(entityItem);
                                    stack.stackSize = 0;
                                }
                            }
                        }
                        operation = TreeFarmOperation.PLANTING;
                    } else if (operation.equals(TreeFarmOperation.PLANTING)) {
                        operation = TreeFarmOperation.SCANNING;
                    }
                }
            }
            BlockPos blockPos = pos.add(scanx, 1, scanz);

            if (operation.equals(TreeFarmOperation.SCANNING)) {
                List<BlockPos> newBlockPoses = ItemChainsaw.getSortedWoodList(blockPos, worldObj);
                if (newBlockPoses != null) {
                    toChop.addAll(newBlockPoses);
                }
                return;
            }
            if (operation.equals(TreeFarmOperation.PLANTING)) {
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
                return;
            }
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
        return capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) ? (T) internalInventory : super.getCapability(capability, facing);
    }
}