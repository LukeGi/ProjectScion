package com.github.projectscion.common.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IItemRenderer {
 @SideOnly(Side.CLIENT)
 void registerItemRenderer();
}
