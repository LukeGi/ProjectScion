package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.items.ItemBase;
import com.bluemonster122.projectscion.common.util.InventoryHelper;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ItemIronMiningTool extends ItemBase {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone, Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone, Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab, Blocks.stone_button, Blocks.stone_pressure_plate);

    public ItemIronMiningTool() {

        super("ironminingtool");
        this.setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        this.setInternalName("ironminingtool");
        this.setMaxStackSize(1);
        this.setMaxDamage(128);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {

        return handleBlockDestroyed(stack, worldIn, pos, entityLiving);
    }

    public static boolean handleBlockDestroyed(ItemStack stack, World worldIn, BlockPos pos, EntityLivingBase entityLiving) {

        if (!(entityLiving instanceof EntityPlayer)) {
            return false;
        }
        if (entityLiving.isSneaking()) {
            stack.damageItem(1, entityLiving);
            return !InventoryHelper.breakBlockIntoPlayerInv(worldIn, pos, stack, (EntityPlayer) entityLiving);
        }
        BlockPos[] toBreak = null;
        EnumFacing facing = entityLiving.rayTrace(10, 1F).sideHit;
        if (facing.getAxis().equals(EnumFacing.Axis.Z)) {
            toBreak = new BlockPos[]{pos, pos.up(), pos.down(), pos.east(), pos.west(), pos.up().west(), pos.up().east(), pos.down().west(), pos.down().east()};
        } else if (facing.getAxis().equals(EnumFacing.Axis.X)) {
            toBreak = new BlockPos[]{pos, pos.up(), pos.down(), pos.north(), pos.south(), pos.up().north(), pos.up().south(), pos.down().north(), pos.down().south()};
        } else if (facing.getAxis().equals(EnumFacing.Axis.Y)) {
            toBreak = new BlockPos[]{pos, pos.north(), pos.east(), pos.south(), pos.west(), pos.north().east(), pos.south().east(), pos.south().west(), pos.north().west()};
        }
        if (toBreak == null) {
            stack.damageItem(1, entityLiving);
            return !InventoryHelper.breakBlockIntoPlayerInv(worldIn, pos, stack, (EntityPlayer) entityLiving);
        }
        boolean flag = false;
        for (BlockPos blockPos : toBreak) {
            flag |= InventoryHelper.breakBlockIntoPlayerInv(worldIn, blockPos, stack, (EntityPlayer) entityLiving);
        }
        if (flag) {
            stack.damageItem(1, entityLiving);
            return true;
        } else {
            return true;
        }
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state) {

        Material material = state.getMaterial();
        return material != Material.grass || material != Material.ground || material != Material.iron && material != Material.anvil && material != Material.rock ? super.getStrVsBlock(stack, state) : 20.0F;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {

        Material material = blockIn.getMaterial();
        return !(blockIn.getBlock() instanceof BlockObsidian) && (material == Material.ground || material == Material.grass || material == Material.rock || (material == Material.iron || material == Material.anvil));
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {

        return 2; // Iron Level
    }
}
