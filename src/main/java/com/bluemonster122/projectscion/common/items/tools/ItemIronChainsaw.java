package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.items.ItemBase;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class ItemIronChainsaw extends ItemBase {

    public ItemIronChainsaw() {

        super("ironchainsaw");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("ironchainsaw");
        setMaxStackSize(1);
        setMaxDamage(875);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {

        if (player.worldObj.getBlockState(pos).getBlock().isWood(player.worldObj, pos)) {
            return true;
        } else {
            return super.onBlockStartBreak(itemstack, pos, player);
        }
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state) {

        Material material = state.getMaterial();
        return material != Material.wood && material != Material.leaves ? super.getStrVsBlock(stack, state) : 8.0F;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {

        Material material = blockIn.getMaterial();
        return material == Material.leaves || material == Material.wood;
    }
}
