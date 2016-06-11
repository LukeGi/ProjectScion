package com.github.projectscion.common.features.treefarm;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.core.multiblock.BlockMultiblock;
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
public class BlockTreeFarm extends BlockMultiblock {

    public BlockTreeFarm() {

        super(Material.IRON);
        setCreativeTab(CreativeTabs.REDSTONE);
        setRegistryName(new ResourceLocation(ModInfo.MOD_ID, "tree_farm"));
        setUnlocalizedName(getRegistryName().toString());
    }

    //TODO: add the method that drops a stack on rightclick, and drops 1 item on shift rightclick, and drop the entire inventory on ctrl rightclick.

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {

        return new TileEntityTreeFarm();
    }
}
