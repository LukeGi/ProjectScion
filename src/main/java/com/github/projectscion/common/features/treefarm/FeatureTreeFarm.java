package com.github.projectscion.common.features.treefarm;

import com.github.projectscion.common.features.Feature;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class FeatureTreeFarm extends Feature {
    public static final BlockTreeFarm tree_farm = new BlockTreeFarm();

    @Override
    public void registerBlocks() {

        GameRegistry.register(tree_farm);
        GameRegistry.register(new ItemBlock(tree_farm).setRegistryName(tree_farm.getRegistryName()));
    }

    @Override
    public void registerTileEntities() {

        GameRegistry.registerTileEntity(TileEntityTreeFarm.class, tree_farm.getRegistryName().getResourcePath());
    }
}
