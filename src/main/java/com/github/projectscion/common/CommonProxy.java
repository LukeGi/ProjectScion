package com.github.projectscion.common;

import com.github.projectscion.common.features.Features;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class CommonProxy {

	public void preinit(FMLPreInitializationEvent event) {
		Features.registerCongifurations(event.getSuggestedConfigurationFile());
		Features.registerEvents();
		Features.registerItems();
		Features.registerBlocks();
		Features.registerTileEntityies();
	}

	public void init(FMLInitializationEvent event) {
		Features.registerRecipes();
	}

	public void postinit(FMLPostInitializationEvent event) {

	}
}
