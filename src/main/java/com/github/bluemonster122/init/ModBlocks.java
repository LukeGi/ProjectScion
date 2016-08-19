package com.github.bluemonster122.init;

import java.util.ArrayList;
import java.util.List;

import com.github.bluemonster122.ProjectScion;
import com.github.bluemonster122.blocks.BlockBasePS;
import com.github.bluemonster122.blocks.BlockBasePSTile;
import com.github.bluemonster122.utils.IRegister;
import com.github.bluemonster122.utils.IRegisterRenderer;

import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

@ObjectHolder(value = ProjectScion.MOD_ID)
public class ModBlocks implements IRegister, IRegisterRenderer {

	public static final List<BlockBasePS> BLOCKS = new ArrayList<>();

	@Override
	public void register() {
		registerBlocks();
		registerItemBlocks();
		registerTileEntities();
	}

	private void registerBlocks() {
		BLOCKS.parallelStream().forEach(BlockBasePS::registerBlock);
	}

	private void registerItemBlockRenders() {
		BLOCKS.parallelStream().forEach(BlockBasePS::registerItemBlockRenders);
	}

	private void registerItemBlocks() {
		BLOCKS.parallelStream().forEach(BlockBasePS::registerItemBlock);
	}

	@Override
	public void registerRenders() {
		registerItemBlockRenders();
	}

	private void registerTileEntities() {
		BLOCKS.parallelStream().forEach(block -> {
			if (block instanceof BlockBasePSTile)
				((BlockBasePSTile) block).registerTileEntity();
		});
	}
}
