package com.github.projectscion.common.features.treefarm;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.core.multiblock.BlockMultiblock;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

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

 @Override
 public TileEntity createNewTileEntity(World worldIn, int meta) {

  return new TileEntityTreeFarm();
 }
}
