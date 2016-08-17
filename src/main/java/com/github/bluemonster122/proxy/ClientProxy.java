package com.github.bluemonster122.proxy;

import com.github.bluemonster122.ProjectScion;
import com.github.bluemonster122.utils.IRegisterRenderer;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ProjectScion.renderRegisters.forEach(IRegisterRenderer::registerRenders);
	}
}
