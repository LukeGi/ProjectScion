package com.github.bluemonster122.proxy;

import com.github.bluemonster122.ProjectScion;
import com.github.bluemonster122.utils.IRegister;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public abstract class CommonProxy implements IProxy {
	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ProjectScion.registers.forEach(IRegister::register);
	}
}
