package com.github.projectscion.common.features.treefarm;

import com.github.projectscion.common.core.multiblock.TileEntityMultiblock;
import com.github.projectscion.common.features.tools.ItemChainsaw;
import com.github.projectscion.common.util.LogHelper;
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
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

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
        moveScanner();
        if (operation.equals(TreeFarmOperation.NONE)) {
            operation = TreeFarmOperation.CUTTING;
        }
        if (operation.equals(TreeFarmOperation.CUTTING)) {
            cutTree();
        }
        if (operation.equals(TreeFarmOperation.SCANNING) || operation.equals(TreeFarmOperation.PLANTING)) {
            BlockPos blockPos = pos.add(scanx, 1, scanz);

            if (operation.equals(TreeFarmOperation.SCANNING)) {
                scan(blockPos);
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

    private void scan(BlockPos blockPos) {
        BiFunction<BlockPos, World, Boolean> function = worldObj.getBlockState(getPos().down()).getBlock().equals(Blocks.IRON_BLOCK) ? ((p, w) -> (w.getBlockState(p).getBlock().isWood(w, p) || w.getBlockState(p).getBlock().isLeaves(w.getBlockState(p), w, p))) : ((p, w) -> (w.getBlockState(p).getBlock().isWood(w, p)));
        List<BlockPos> newBlockPoses = ItemChainsaw.getSortedList(blockPos, worldObj, function);
        if (newBlockPoses != null) {
            toChop.addAll(newBlockPoses);
        }
    }

    private void moveScanner() {
        scanx++;
        if (scanx == 3) {
            scanz++;
            scanx = -2;
            if (scanz == 3) {
                scanz = -2;
                if (operation.equals(TreeFarmOperation.SCANNING)) {
                    pickupDrops();
                    operation = TreeFarmOperation.CUTTING;
                    return;
                }
                if (operation.equals(TreeFarmOperation.PLANTING)) {
                    operation = TreeFarmOperation.SCANNING;
                    return;
                }
            }
        }
        if (operation.equals(TreeFarmOperation.CUTTING) && toChop.isEmpty()) {
            scanx = -2;
            scanz = -2;
            LogHelper.info(scanx + " " + scanz);
            operation = TreeFarmOperation.PLANTING;
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
    }

    private void cutTree() {
        if (toChop.isEmpty()) return;
        List<BlockPos> woods = toChop;

        for (int i = 0; i < woods.size(); i++) {
            BlockPos coord = woods.get(i);
            Block block = getWorld().getBlockState(coord).getBlock();

            List<ItemStack> drops;
            drops = block.getDrops(getWorld(), coord, getWorld().getBlockState(coord), 0);

            handleDrops(drops);

            // CONSUME POWER

            getWorld().setBlockToAir(coord);
            damageTool();
        }
        toChop.clear();

//
//        BlockPos blockPos = toChop.get(0);
//        toChop.remove(0);
//        worldObj.destroyBlock(blockPos, true);
//        if (toChop.isEmpty()) {
//            operation = TreeFarmOperation.SCANNING;
//        }
    }

    private void damageTool() {
        List<IItemHandler> possibleIOs = new ArrayList<>();
        for (int i = 0; i < multiblock.size(); i++) {
            BlockPos coord = multiblock.get(i).up(1);
            TileEntity tile = getWorld().getTileEntity(coord);
            IItemHandler itemHandler = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);

        }
    }

    private void handleDrops(List<ItemStack> drops) {
        for (int i = 0; i < drops.size(); i++) {
            ItemStack drop = drops.get(i);
            EntityItem item = new EntityItem(getWorld(), pos.getX() + 0.5, pos.getY() + 3, pos.getZ() + 0.5, drop);
            item.setPickupDelay(200);
            item.setThrower(this.toString());
            item.setVelocity(0, 0, 0);
            getWorld().spawnEntityInWorld(item);
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
}