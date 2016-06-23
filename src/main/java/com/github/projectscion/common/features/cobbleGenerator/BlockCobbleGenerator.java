package com.github.projectscion.common.features.cobbleGenerator;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.core.multiblock.BlockMultiblock;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class BlockCobbleGenerator extends BlockMultiblock {

    public BlockCobbleGenerator() {
        super(Material.ROCK);
        setRegistryName(ModInfo.MOD_ID, "cobble_generator");
        setUnlocalizedName(getRegistryName().toString());
        setCreativeTab(CreativeTabs.REDSTONE);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityCobbleGenerator();
    }
}
