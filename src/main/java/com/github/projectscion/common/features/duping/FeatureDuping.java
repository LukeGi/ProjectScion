package com.github.projectscion.common.features.duping;

import com.github.projectscion.common.features.Feature;

/**
 * Created by blue on 02/07/16.
 */
public class FeatureDuping extends Feature {
 public static final ItemDupePaste DUPE_PASTE = new ItemDupePaste();
 public static final BlockTransformingBlock TRANSFORMING_BLOCK = new BlockTransformingBlock();

 @Override
 public void preInit() {
  registerFeature(DUPE_PASTE);
  registerFeature(TRANSFORMING_BLOCK, BlockTransformingBlock.TileEntityTransformingBlock.class);
 }
}
