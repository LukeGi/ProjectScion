package com.github.projectscion.common.features.treefarm;

import com.github.projectscion.common.features.Feature;

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
