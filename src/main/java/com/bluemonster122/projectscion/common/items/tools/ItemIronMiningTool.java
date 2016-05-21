package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.items.ItemBase;
import com.bluemonster122.projectscion.common.items.Items;
import com.bluemonster122.projectscion.common.util.IProvideRecipe;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Set;

public class ItemIronMiningTool extends ItemBase implements IProvideRecipe {

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

        BlockPos blockPos;
        EnumFacing facing = entityLiving.getHorizontalFacing();
        if (entityLiving.getLookVec().yCoord > 0.5)
            facing = EnumFacing.UP;
        if (entityLiving.getLookVec().yCoord < -0.5)
            facing = EnumFacing.DOWN;
        // Center
        blockPos = pos;
        boolean flag = breakBlock(worldIn, blockPos);
        // Sides
        for (EnumFacing facing1 : EnumFacing.VALUES) {
            if (facing.getAxis().equals(facing1.getAxis()))
                continue;
            blockPos = pos.offset(facing1);
            flag |= breakBlock(worldIn, blockPos);
        }
        //Corners
        for (EnumFacing facing1 : EnumFacing.VALUES) {
            for (EnumFacing facing2 : EnumFacing.VALUES) {
                if (facing.getAxis().equals(facing1.getAxis()))
                    continue;
                if (facing.getAxis().equals(facing2.getAxis()))
                    continue;
                if (facing1.equals(facing2))
                    continue;
                blockPos = pos.offset(facing1).offset(facing2);
                flag |= breakBlock(worldIn, blockPos);
            }
        }
        if (flag)
            stack.damageItem(1, entityLiving);
        return flag;
    }

    private boolean breakBlock(World world, BlockPos pos) {

        if (this.canHarvestBlock(world.getBlockState(pos)))
            return world.destroyBlock(pos, true);
        return false;
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state) {

        Material material = state.getMaterial();
        return material != Material.grass || material != Material.ground || material != Material.iron && material != Material.anvil && material != Material.rock ? super.getStrVsBlock(stack, state) : 20.0F;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {

        Material material = blockIn.getMaterial();
        return !(blockIn.getBlock() instanceof BlockObsidian)  && (material == Material.ground || material == Material.grass || material == Material.rock || (material == Material.iron || material == Material.anvil));
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {

        return 2; // Iron Level
    }

    @Override
    public void RegisterRecipes() {

        GameRegistry.addShapedRecipe(Items.IRON_MINING_TOOL.getStack(1), "  I", " I ", "H  ", 'H', Items.TOOL_HANDLE.getStack(1), 'I', new ItemStack(net.minecraft.init.Items.iron_ingot, 1));
    }
}
