package com.bluemonster122.projectscion.common.tileentities.farm;

import com.bluemonster122.projectscion.common.inventory.InventoryOperation;
import com.bluemonster122.projectscion.common.items.tools.ItemIronChainsaw;
import com.bluemonster122.projectscion.common.util.InventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class TileEntityTreeFarm extends TileEntityFarm implements ITickable {

    private ItemStackHandler internalInventory = new ItemStackHandler(27);
    private List<BlockPos> toChop = new ArrayList<>();

    public void save(NBTTagCompound nbt) {

        if (getAreaPo() != null) {
            nbt.setInteger("areaX", getAreaPo().getX());
            nbt.setInteger("areaY", getAreaPo().getY());
            nbt.setInteger("areaZ", getAreaPo().getZ());
        }
    }

    public void load(NBTTagCompound nbt) {

        if (nbt.hasKey("areaX") && nbt.hasKey("areaY") && nbt.hasKey("areaZ")) {
            setArea(new BlockPos(nbt.getInteger("areaX"), nbt.getInteger("areaY"), nbt.getInteger("areaZ")));
        }
    }

    @Override
    public void update() {

        if (getArea() != null) {
            if (worldObj.getTotalWorldTime() % 100 == 20) {
                // COLLECT DROPS
                if (getArea().getAABB() != null) {
                    List<ItemStack> acceptedDrops = new ArrayList<>();
                    for (ItemStack itemStack : OreDictionary.getOres("treeSapling")) {
                        acceptedDrops.add(itemStack);
                    }
                    for (ItemStack itemStack : OreDictionary.getOres("logWood")) {
                        acceptedDrops.add(itemStack);
                    }
                    List<EntityItem> loots = worldObj.getEntitiesWithinAABB(EntityItem.class, getArea().getAABB());
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
                        InventoryHelper.addItemStackToInventory(loot.getEntityItem(), this, 0, 99);
                        loot.setDead();
                    }
                }
            }
            if (getArea().getInside() != null && worldObj.getTotalWorldTime() % 100 == 40) {
                // CUT BLOCKS
                for (int i = getArea().getInside().size() - 1; i >= 0; i--) {
                    List<BlockPos> positiosn = ItemIronChainsaw.getSortedWoodList(getArea().getInside().get(i), worldObj);
                    if (positiosn != null) {
                        toChop.addAll(positiosn);
                    }
                }
            }
            if (getArea().getInside() != null && worldObj.getTotalWorldTime() % 10 == 1) {
                //PLANT SAPLINGS
                for (int s = getArea().getInside().size() - 1; s >= 0; s--) {
                    BlockPos p = getArea().getInside().get(s);
                    Block block = worldObj.getBlockState(p).getBlock();
                    TileEntity tile = worldObj.getTileEntity(pos);
                    if (!(tile instanceof IInventory))
                        return;
                    IInventory output = (IInventory) tile;
                    if (block.isReplaceable(worldObj, p))
                        for (int i = 0; i < output.getSizeInventory(); i++) {
                            ItemStack stack = output.getStackInSlot(i);
                            if (stack != null && stack.getItem() instanceof ItemBlock) {
                                block = ((ItemBlock) stack.getItem()).getBlock();
                                if (block.getUnlocalizedName().toLowerCase().contains("sapling") && ((BlockSapling) block).canBlockStay(worldObj, p, block.getStateFromMeta(stack.getItemDamage()))) {
                                    worldObj.setBlockState(p, block.getStateFromMeta(stack.getItemDamage()), 3);
                                    output.decrStackSize(i, 1);
                                    break;
                                }
                            }
                        }
                }
            }
        }
        if (!toChop.isEmpty()) {
            for (BlockPos pos : toChop) {
                worldObj.destroyBlock(pos, true);
            }
            toChop.clear();
        }
    }

    @Override
    public void onChunkLoad() {

        initMachineData();
        super.onChunkLoad();
    }

    @Override
    public ItemStackHandler getInternalInventory() {

        return internalInventory;
    }

    @Override
    public void onChangeInventory(IInventory inv, int slot, InventoryOperation operation, ItemStack removed, ItemStack added) {

    }

    @Override
    public int[] getAccessibleSlotsBySide(EnumFacing side) {

        return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 22, 23, 24, 25, 26};
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
