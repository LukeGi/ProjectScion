package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.items.ItemBase;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ItemDiamondMiningTool extends ItemBase {

    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.activator_rail, Blocks.coal_ore, Blocks.cobblestone, Blocks.detector_rail, Blocks.diamond_block, Blocks.diamond_ore, Blocks.double_stone_slab, Blocks.golden_rail, Blocks.gold_block, Blocks.gold_ore, Blocks.ice, Blocks.iron_block, Blocks.iron_ore, Blocks.lapis_block, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.mossy_cobblestone, Blocks.netherrack, Blocks.packed_ice, Blocks.rail, Blocks.redstone_ore, Blocks.sandstone, Blocks.red_sandstone, Blocks.stone, Blocks.stone_slab, Blocks.stone_button, Blocks.stone_pressure_plate);

    public ItemDiamondMiningTool() {

        super("diamondminingtool");
        this.setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        this.setInternalName("diamondminingtool");
        this.setMaxStackSize(1);
        this.setMaxDamage(512);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState blockIn, BlockPos pos, EntityLivingBase entityLiving) {

        return ItemIronMiningTool.handleBlockDestroyed(stack, worldIn, pos, entityLiving);
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state) {

        Material material = state.getMaterial();
        return material != Material.ground && material != Material.grass && material != Material.iron && material != Material.anvil && material != Material.rock ? super.getStrVsBlock(stack, state) : 12.0F;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {

        Material material = blockIn.getMaterial();
        return material == Material.ground || material == Material.grass || material == Material.rock || (material == Material.iron || material == Material.anvil);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {

        return 3; // Iron Level
    }
}
