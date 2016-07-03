package com.github.projectscion.common.features.tools;

import com.github.projectscion.common.features.Feature;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class FeatureTool extends Feature {

 public static final ItemToolHandle TOOL_HANDLE = new ItemToolHandle();
 public static final ItemMiningTool MINING_TOOL_IRON = new ItemMiningTool("iron");
 public static final ItemMiningTool MINING_TOOL_DIAMOND = new ItemMiningTool("diamond");
 public static final ItemChainsaw CHAINSAW = new ItemChainsaw("iron");
 public static final ItemRythmicPickaxe RYTHMIC_PICKAXE = new ItemRythmicPickaxe();

 @Override
 public void preInit() {
  registerFeature(TOOL_HANDLE);
  registerFeature(MINING_TOOL_IRON);
  registerFeature(MINING_TOOL_DIAMOND);
  registerFeature(CHAINSAW);
  registerFeature(RYTHMIC_PICKAXE);
 }

 @Override
 public void init() {
  ItemStack enchanted_instant_pickaxe = new ItemStack(RYTHMIC_PICKAXE, 1);
  enchanted_instant_pickaxe.addEnchantment(Enchantment.getEnchantmentByID(33), 1);
  GameRegistry.addShapelessRecipe(enchanted_instant_pickaxe, new ItemStack(RYTHMIC_PICKAXE, 1), new ItemStack(Items.RECORD_MALL, 1));

 }
}
