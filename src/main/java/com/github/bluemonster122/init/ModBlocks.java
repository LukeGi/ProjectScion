package com.github.bluemonster122.init;

import java.util.ArrayList;
import java.util.List;

import com.github.bluemonster122.ProjectScion;
import com.github.bluemonster122.blocks.BlockBasePS;
import com.github.bluemonster122.utils.IRegister;
import com.github.bluemonster122.utils.IRegisterRenderer;

import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(value = ProjectScion.MOD_ID)
public class ModBlocks implements IRegister, IRegisterRenderer {

	public static final List<BlockBasePS> BLOCKS = new ArrayList<>();

	@Override
	public void register() {
		BLOCKS.forEach(BlockBasePS::register);
	}

	@Override
	public void registerRenders() {
		BLOCKS.forEach(BlockBasePS::registerRenders);
	}

}
