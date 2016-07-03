package com.github.projectscion.common.features.randomthings;

import com.github.projectscion.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;

/**
 * Created by blue on 23/06/16.
 */
public class ItemRandomFood extends ItemFood {
 public ItemRandomFood(String name, int foodVal, int satVal, boolean tame) {
  super(foodVal, satVal, tame);
  setRegistryName(ModInfo.MOD_ID, name);
  setUnlocalizedName(getRegistryName().getResourcePath());
  setCreativeTab(CreativeTabs.FOOD);
 }
}
