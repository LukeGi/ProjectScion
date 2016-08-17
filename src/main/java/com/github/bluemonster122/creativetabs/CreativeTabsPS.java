package com.github.bluemonster122.creativetabs;

import com.github.bluemonster122.ProjectScion;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class CreativeTabsPS {
	public static final CreativeTabs MAIN = new CreativeTabs(ProjectScion.MOD_ID.concat(".main")) {

		@Override
		public Item getTabIconItem() {
			return Items.CLOCK;
		}
	};
}
