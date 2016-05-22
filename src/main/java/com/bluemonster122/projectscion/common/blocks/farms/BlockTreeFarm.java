package com.bluemonster122.projectscion.common.blocks.farms;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.blocks.BlockTileBase;
import com.bluemonster122.projectscion.common.items.ModItems;
import com.bluemonster122.projectscion.common.tileentities.farm.TileEntityTreeFarm;
import com.bluemonster122.projectscion.common.util.LogHelper;
import com.bluemonster122.projectscion.common.util.TileHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

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
