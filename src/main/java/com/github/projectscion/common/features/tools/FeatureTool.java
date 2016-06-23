package com.github.projectscion.common.features.tools;

import com.github.projectscion.common.features.Feature;

public class FeatureTool extends Feature {

    public static final ItemToolHandle tool_handle = new ItemToolHandle();
    public static final ItemMiningTool mining_tool_iron = new ItemMiningTool("iron");
    public static final ItemMiningTool mining_tool_diamond = new ItemMiningTool("diamond");
    public static final ItemChainsaw chainsaw_iron = new ItemChainsaw("iron");
    public static final ItemAreaDesignator area_designator = new ItemAreaDesignator();

    @Override
    public void preInit() {
        registerFeature(tool_handle);
        registerFeature(mining_tool_iron);
        registerFeature(mining_tool_diamond);
        registerFeature(chainsaw_iron);
        registerFeature(area_designator);
    }
}
