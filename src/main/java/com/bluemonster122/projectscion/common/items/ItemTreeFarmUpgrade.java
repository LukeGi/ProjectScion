package com.bluemonster122.projectscion.common.items;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.blocks.ModBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class ItemTreeFarmUpgrade extends ItemBase {

    public ItemTreeFarmUpgrade() {

        super("tree_farm_upgrade");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("tree_farm_upgrade");
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (worldIn.getBlockState(pos).getBlock() == ModBlocks.FARM.getBlock()) {
            if (worldIn.isRemote) {
                for (int i = 0; i < 10; i++) {
                    worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.offset(facing).getX(), pos.offset(facing).getY() , pos.offset(facing).getZ(), 0, 0.01, 0, new int[0]);
                }
            } else {
                worldIn.setBlockState(pos, ModBlocks.TREE_FARM.getBlock().getDefaultState());
                playerIn.setHeldItem(hand, null);
            }
        }
        return super.onItemUse(stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }
}
