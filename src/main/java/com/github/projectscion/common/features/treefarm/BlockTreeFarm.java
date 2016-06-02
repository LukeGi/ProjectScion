package com.github.projectscion.common.features.treefarm;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.util.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class BlockTreeFarm extends Block implements ITileEntityProvider {

    public BlockTreeFarm() {

        super(Material.IRON);
        setCreativeTab(CreativeTabs.REDSTONE);
        setRegistryName(new ResourceLocation(ModInfo.MOD_ID, "tree_farm"));
        setUnlocalizedName("tree_farm");
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        // TODO: reimplement this
//        if (!worldIn.isRemote) {
//            TileEntityTreeFarm farm = (TileEntityTreeFarm) worldIn.getTileEntity(pos);
//            ItemStackHandler farmInternalInventory = farm.getInternalInventory();
//            boolean droppedAStack = false;
//            for (int i = 0; i < farmInternalInventory.getSlots() && !droppedAStack; i++) {
//                if (farmInternalInventory.getStackInSlot(i) != null) {
//                    droppedAStack = true;
//                    ItemStack toDrop = farmInternalInventory.getStackInSlot(i);
//                    farmInternalInventory.setStackInSlot(i, null);
//                    InventoryHelper.addItemStackToInventory(toDrop, playerIn.inventory, 0, playerIn.inventory.mainInventory.length);
//                }
//            }
//        }
        LogHelper.info(String.valueOf(((TileEntityTreeFarm) worldIn.getTileEntity(pos)).isFormed()));
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
//        final TileEntityTreeFarm tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityTreeFarm.class);
//        if (tileEntity != null) {
//            tileEntity.dropItems();
//        }
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        return willHarvest || super.removedByPlayer(state, world, pos, player, false);
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        super.harvestBlock(worldIn, player, pos, state, te, stack);
        worldIn.setBlockToAir(pos);
    }

    /**
     * Add machineItemData to item that drops
     *
     * @param world
     * @param pos
     * @param state
     * @param fortune
     * @return
     */
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
//        TileEntityTreeFarm tileEntityBase = TileHelper.getTileEntity(world, pos, TileEntityTreeFarm.class);
//        if (tileEntityBase != null) {
//            final ItemStack itemStack = new ItemStack(this, 1, tileEntityBase.getBlockMetadata());
//
//            NBTTagCompound machineItemData = tileEntityBase.getMachineItemData();
//            if (machineItemData != null) {
//                NBTTagCompound itemTag = new NBTTagCompound();
//                itemTag.setTag("MachineItemData", machineItemData);
//                itemStack.setTagCompound(itemTag);
//            }
//
//            if (tileEntityBase.hasCustomName()) {
//                itemStack.setStackDisplayName(tileEntityBase.getCustomName());
//            }
//
//            ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
//            drops.add(itemStack);
//            return drops;
//        }
        return super.getDrops(world, pos, state, fortune);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityTreeFarm();
    }
}
