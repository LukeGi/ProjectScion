package com.github.projectscion.common.features.magic;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockFertileSand extends Block {
    public BlockFertileSand() {
        super(Material.SAND);
        setHardness(0.6F);
        setHarvestLevel("shovel", 0);
        setHarvestLevel("hoe", 0);
        setRegistryName("fertile_sand");
        setUnlocalizedName(getRegistryName().getResourcePath());
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setTickRandomly(false);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
        if (!direction.equals(EnumFacing.UP)) {
            return false;
        } else {
            EnumPlantType type = plantable.getPlantType(world, pos.up());
            switch (type.ordinal()) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public boolean isFertile(World world, BlockPos pos) {
        return true;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList drops = new ArrayList();
        drops.add(new ItemStack(Blocks.DIRT, 1));
        for (int i = 0; i < 8; i++) {
            if (new Random().nextFloat() > 0.5F) {
                drops.add(new ItemStack(Items.DYE, EnumDyeColor.WHITE.getMetadata(), 1));
            }
        }
        return drops;
    }
}
