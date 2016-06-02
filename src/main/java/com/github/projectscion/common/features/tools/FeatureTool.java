package com.github.projectscion.common.features.tools;

import com.github.projectscion.common.features.Feature;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FeatureTool extends Feature {

	public static final ItemToolHandle tool_handle = new ItemToolHandle();
	public static final ItemMiningTool mining_tool_iron = new ItemMiningTool("iron");
	public static final ItemMiningTool mining_tool_diamond = new ItemMiningTool("diamond");
	public static final ItemChainsaw chainsaw_iron = new ItemChainsaw("iron");

	@Override
	public void registerItems() {
		GameRegistry.register(tool_handle);
		GameRegistry.register(mining_tool_iron);
		GameRegistry.register(mining_tool_diamond);
		GameRegistry.register(chainsaw_iron);
		// if (Platform.isClient()) {
		// RegistrationHelper.registerRender(tool_handle);
		// RegistrationHelper.registerRender(mining_tool_iron);
		// RegistrationHelper.registerRender(mining_tool_diamond);
		// RegistrationHelper.registerRender(chainsaw_iron);
		// }
	}

	@Override
	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(tool_handle);
		MinecraftForge.EVENT_BUS.register(chainsaw_iron);
	}
}
