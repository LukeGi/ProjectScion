package com.github.bluemonster122.init;

import java.util.ArrayList;
import java.util.List;

import com.github.bluemonster122.ProjectScion;
import com.github.bluemonster122.items.ItemBasePS;
import com.github.bluemonster122.utils.IRegister;

import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(value = ProjectScion.MOD_ID)
public class ModItems implements IRegister {

	public static final List<ItemBasePS> ITEMS = new ArrayList<>();

	@Override
	public void register() {
		ITEMS.forEach(ItemBasePS::register);
	}

}
