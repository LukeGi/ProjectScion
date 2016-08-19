package com.github.bluemonster122.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockBasePSTile extends BlockBasePS implements ITileEntityProvider {

	public BlockBasePSTile(@Nullable Material mat, @Nullable CreativeTabs tab) {
		super(mat, tab);
	}

	protected abstract Class<? extends TileEntity> getTEClass();

	public void registerTileEntity() {
		GameRegistry.registerTileEntity(getTEClass(), getRegistryName().getResourcePath());
	}
}
