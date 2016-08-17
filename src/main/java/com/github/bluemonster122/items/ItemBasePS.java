package com.github.bluemonster122.items;

import com.github.bluemonster122.creativetabs.CreativeTabsPS;
import com.github.bluemonster122.utils.IRegister;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public abstract class ItemBasePS extends Item implements IRegister {

	public ItemBasePS() {
		this(CreativeTabsPS.MAIN);
	}

	public ItemBasePS(CreativeTabs tab) {
		setRegistryName(getName());
		setUnlocalizedName(getRegistryName().getResourcePath());
		setCreativeTab(tab);
	}

	protected abstract String getName();

	@Override
	public void register() {
		GameRegistry.register(this);
	}
}
