package com.github.projectscion.common.features.duping;

import com.github.projectscion.ModInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by blue on 02/07/16.
 */
public class ItemDupePaste extends Item {
    public ItemDupePaste() {
        setRegistryName(ModInfo.MOD_ID, "duping_paste");
        setUnlocalizedName(getRegistryName().getResourcePath());
        setCreativeTab(CreativeTabs.MATERIALS);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            attemptTransform(worldIn, pos, Blocks.LAPIS_BLOCK.getDefaultState());
            attemptTransform(worldIn, pos, Blocks.QUARTZ_BLOCK.getDefaultState());
            attemptTransform(worldIn, pos, Blocks.PRISMARINE.getDefaultState());
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    public void attemptTransform(World world, BlockPos pos, IBlockState block) {
        if (world.getBlockState(pos).equals(block)) {
            world.setBlockState(pos, FeatureDuping.TRANSFORMING_BLOCK.getDefaultState(), 11);
            ((BlockTransformingBlock.TileEntityTransformingBlock) world.getTileEntity(pos)).setDrop(new ItemStack(block.getBlock(), 2, block.getBlock().getMetaFromState(block)));
        }
    }
}
