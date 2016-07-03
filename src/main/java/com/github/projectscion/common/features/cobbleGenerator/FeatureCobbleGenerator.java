package com.github.projectscion.common.features.cobbleGenerator;

import com.github.projectscion.common.features.Feature;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class FeatureCobbleGenerator extends Feature {

 public static final BlockCobbleGenerator cobble_generator = new BlockCobbleGenerator();

 @Override
 public void preInit() {
  registerFeature(cobble_generator, TileEntityCobbleGenerator.class);
 }
}
