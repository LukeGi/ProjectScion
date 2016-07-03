package com.github.projectscion.common.features.magic;

import com.github.projectscion.common.features.Feature;

/**
 * Created by blue on 03/07/16.
 */
public class FeatureMagic extends Feature {
 public static final BlockMagicDoor MAGIC_DOOR = new BlockMagicDoor();
 public static final ItemMovingThing MOVING_THING = new ItemMovingThing();

 @Override
 public void preInit() {
  registerFeature(MAGIC_DOOR, BlockMagicDoor.TileEntityMagicDoor.class);
 }
}
