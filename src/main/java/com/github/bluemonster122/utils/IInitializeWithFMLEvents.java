package com.github.bluemonster122.utils;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public interface IInitializeWithFMLEvents {
	void init(FMLInitializationEvent event);

	void postInit(FMLPostInitializationEvent event);

	void preInit(FMLPreInitializationEvent event);
}
