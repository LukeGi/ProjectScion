package com.github.bluemonster122.blocks;

import javax.annotation.Nullable;

import com.github.bluemonster122.creativetabs.CreativeTabsPS;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class BlockBasePS extends Block {

	public BlockBasePS(@Nullable Material mat, @Nullable CreativeTabs tab) {
		super(mat == null ? Material.ROCK : mat);
		setRegistryName(getName());
		setUnlocalizedName(getRegistryName().getResourcePath());
		setCreativeTab(tab == null ? CreativeTabsPS.MAIN : tab);
	}

	protected abstract String getName();

	public void registerBlock() {
		GameRegistry.register(this);
	}

	public void registerItemBlock() {
		GameRegistry.register(new ItemBlock(this).setRegistryName(getRegistryName()));
	}

	public void registerItemBlockRenders() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
				new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
