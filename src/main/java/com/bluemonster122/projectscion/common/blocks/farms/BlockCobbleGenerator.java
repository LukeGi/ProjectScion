package com.bluemonster122.projectscion.common.blocks.farms;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.blocks.BlockTileBase;
import com.bluemonster122.projectscion.common.tileentities.TileEntityAreaDefinition;
import com.bluemonster122.projectscion.common.tileentities.farm.TileEntityCobbleGenerator;
import com.bluemonster122.projectscion.common.util.InventoryHelper;
import com.bluemonster122.projectscion.common.util.LogHelper;
import com.bluemonster122.projectscion.common.util.TileHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
// TODO: 25/05/2016 Add a recipe for this... something to do with water, lava, an iron block and a drill.
public class BlockCobbleGenerator extends BlockTileBase {

    public BlockCobbleGenerator() {

        super(Material.rock, "cobble_gen");
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTileEntity(TileEntityCobbleGenerator.class);
        this.setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        this.setInternalName("cobble_gen");
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {

        TileEntity tile = worldIn.getTileEntity(pos);
        if (tile instanceof TileEntityCobbleGenerator) {
            TileEntityCobbleGenerator tileCobbleGen = (TileEntityCobbleGenerator) tile;
            if (!worldIn.isRemote && tileCobbleGen.isFormed()) {
                LogHelper.info(String.valueOf(tileCobbleGen.getCobbleStored()));
                ItemStack cobble = tileCobbleGen.getInternalInventory().getStackInSlot(0);
                tileCobbleGen.setCobbleStored(0);
                worldIn.spawnEntityInWorld(new EntityItem(worldIn, pos.up().getX() + 0.5, pos.up().getY(), pos.up().getZ() + 0.5, cobble));
                return true;
            } else {
                tileCobbleGen.initMachineData();
                return true;
            }
        }
        return false;
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

        TileEntityAreaDefinition tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityAreaDefinition.class);
        if (tileEntity != null) {
            return state.withProperty(FACING, tileEntity.getForward());
        }
        return state.withProperty(FACING, EnumFacing.NORTH);
    }
}
