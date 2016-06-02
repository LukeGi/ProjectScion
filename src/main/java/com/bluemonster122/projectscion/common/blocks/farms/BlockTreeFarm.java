package com.bluemonster122.projectscion.common.blocks.farms;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.blocks.BlockTileBase;
import com.bluemonster122.projectscion.common.inventory.InternalInventory;
import com.bluemonster122.projectscion.common.items.ModItems;
import com.bluemonster122.projectscion.common.tileentities.farm.TileEntityTreeFarm;
import com.bluemonster122.projectscion.common.util.InventoryHelper;
import com.bluemonster122.projectscion.common.util.LogHelper;
import com.bluemonster122.projectscion.common.util.TileHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class BlockTreeFarm extends BlockTileBase {

    public BlockTreeFarm() {

        super(Material.iron, "tree_farm");
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTileEntity(TileEntityTreeFarm.class);
        this.setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        this.setInternalName("tree_farm");
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        // TODO: 24/05/2016 test if this method does what i wanted
        if (!worldIn.isRemote) {
            TileEntityTreeFarm farm = (TileEntityTreeFarm) worldIn.getTileEntity(pos);
            ItemStackHandler farmInternalInventory = farm.getInternalInventory();
            boolean droppedAStack = false;
            for (int i = 0; i < farmInternalInventory.getSlots() && !droppedAStack; i++) {
                if (farmInternalInventory.getStackInSlot(i) != null) {
                    droppedAStack = true;
                    ItemStack toDrop = farmInternalInventory.getStackInSlot(i);
                    farmInternalInventory.setStackInSlot(i, null);
                    InventoryHelper.addItemStackToInventory(toDrop, playerIn.inventory, 0, playerIn.inventory.mainInventory.length);
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {

        TileEntityTreeFarm tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityTreeFarm.class);
        if (tileEntity != null) {
            return state.withProperty(FACING, tileEntity.getForward());
        }
        return state.withProperty(FACING, EnumFacing.NORTH);
    }
}
