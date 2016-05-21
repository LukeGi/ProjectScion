package com.bluemonster122.projectscion.common.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IOrientableBlock {
    boolean usesMetaData();

    IOrientable getOrientable(IBlockAccess world, BlockPos pos);
}
