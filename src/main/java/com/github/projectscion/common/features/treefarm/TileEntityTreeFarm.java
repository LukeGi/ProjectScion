package com.github.projectscion.common.features.treefarm;

import com.github.projectscion.common.core.multiblock.TileEntityMultiblock;
import com.github.projectscion.common.features.tools.ItemChainsaw;
import com.mojang.realmsclient.util.Pair;
import com.sun.istack.internal.NotNull;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
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
        multiblock.add(pos);
        multiblock.add(pos.up());
        multiblock.add(pos.up(2));
        // Largest Circle.
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                multiblock.add(pos.up(3).north(z).east(x));
            }
        }
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                multiblock.add(pos.up(4).north(z).east(x));
            }
        }
        for (int x = -1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                multiblock.add(pos.up(5).north(z).east(x));
            }
        }
        multiblock.add(pos.up(6));
    }

    private ItemStackHandler internalInventory = new ItemStackHandler(27);
    private List<BlockPos> toChop = new ArrayList<>();
    private Pair<BlockPos, BlockPos> corners;

    public void save(NBTTagCompound nbt) {

        boolean saveCorners = corners != null;
        nbt.setBoolean("savedCorners", saveCorners);
        if (saveCorners) {
            nbt.setInteger("corner0X", corners.first().getX());
            nbt.setInteger("corner0Y", corners.first().getY());
            nbt.setInteger("corner0Z", corners.first().getZ());
            nbt.setInteger("corner1X", corners.second().getX());
            nbt.setInteger("corner1Y", corners.second().getY());
            nbt.setInteger("corner1Z", corners.second().getZ());
        }
        nbt.setTag("inventory", internalInventory.serializeNBT());
    }

    public void load(NBTTagCompound nbt) {

        boolean saveCorners = nbt.getBoolean("savedCorners");
        if (saveCorners) {
            corners = Pair.of(new BlockPos(nbt.getInteger("corner0X"), nbt.getInteger("corner0Y"), nbt.getInteger("corner0Z")), new BlockPos(nbt.getInteger("corner1X"), nbt.getInteger("corner1Y"), nbt.getInteger("corner1Z")));
        }
        internalInventory.deserializeNBT(nbt.getCompoundTag("inventory"));
    }

    @Override
    public void function() {
        if (corners != null) {
            if (getWorld().getTotalWorldTime() % 100 == 20) {
                //SAPLINGS
                List<BlockPos> blockPoses = getPositionsInsideCorners(corners);
                for (BlockPos blockPos : blockPoses) {
                    Block block = worldObj.getBlockState(blockPos).getBlock();
                    TileEntity tile = worldObj.getTileEntity(pos);
                    if (!(tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN))) {
                        return;
                    }
                    IItemHandler output = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
                    if (block.isReplaceable(worldObj, blockPos))
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
            if (getWorld().getTotalWorldTime() % 100 == 40) {
                //SCAN
                List<BlockPos> blockPoses = getPositionsInsideCorners(corners);
                for (BlockPos blockPos : blockPoses) {
                    List<BlockPos> newBlockPoses = ItemChainsaw.getSortedWoodList(blockPos, worldObj);
                    if (newBlockPoses != null) {
                        toChop.addAll(newBlockPoses);
                    }
                }
            }
            if (getWorld().getTotalWorldTime() % 100 == 60) {
                //PICKUP
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
                                entityItem.getEntityItem().setTagCompound((NBTTagCompound) stack.getTagCompound().copy());
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
            }
            if (getWorld().getTotalWorldTime() % 20 == 1 && !toChop.isEmpty()) {
                //BREAK LOGS
                BlockPos blockPos = toChop.get(0);
                toChop.remove(0);
                worldObj.destroyBlock(blockPos, true);
            }
        }
    }

    private List<BlockPos> getPositionsInsideCorners(Pair<BlockPos, BlockPos> corners) {

        int x1 = corners.first().getX();
        int x2 = corners.second().getX();
        int z1 = corners.first().getZ();
        int z2 = corners.second().getZ();
        List<BlockPos> output = new ArrayList<>();
        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                output.add(new BlockPos(x, corners.first().getY(), z));
            }
        }
        return output;
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

        if (corners != null) {
            int x1 = corners.first().getX();
            int x2 = corners.second().getX();
            int z1 = corners.first().getZ();
            int z2 = corners.second().getZ();
            AxisAlignedBB output = new AxisAlignedBB(x1, corners.first().getY(), z1, x2, corners.first().getY(), z2);
            return output;
        } else {
            return null;
        }
    }
}
//