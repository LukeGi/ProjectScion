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
    public void preInit() {
        registerFeature(tree_farm, TileEntityTreeFarm.class);
    }
}
