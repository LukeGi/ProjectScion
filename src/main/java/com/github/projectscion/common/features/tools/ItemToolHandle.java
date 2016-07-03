package com.github.projectscion.common.features.tools;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.util.IProvideEvent;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Random;

public class ItemToolHandle extends Item implements IProvideEvent {

 public ItemToolHandle() {

  super();
  setCreativeTab(CreativeTabs.TOOLS);
  setRegistryName(ModInfo.MOD_ID, "tool_handle");
  setUnlocalizedName(getRegistryName().toString());
  setMaxStackSize(1);
  setHasSubtypes(true);
 }

 public static void changeItemTo(World worldIn, BlockPos pos, EntityPlayer player, ItemStack item, int i) {

  player.setHeldItem(EnumHand.MAIN_HAND, item);
  BlockPos p;
  for (int j = 0; i >= j; j++) {
   p = pos.up(j);
   worldIn.destroyBlock(p, false);
   worldIn.addWeatherEffect(new EntityLightningBolt(worldIn, p.getX() + 0.5, p.getY(), p.getZ() + 0.5, (new Random()).nextBoolean()));
  }
 }

 @Override
 public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

  if (worldIn.getBlockState(pos).getBlock() == Blocks.IRON_BLOCK) {
   Block above = worldIn.getBlockState(pos.up()).getBlock();
   if (above.equals(Blocks.DIAMOND_BLOCK)) {
    above = worldIn.getBlockState(pos.up(2)).getBlock();
    if (above.equals(Blocks.STONE)) {
     changeItemTo(worldIn, pos, playerIn, new ItemStack(FeatureTool.MINING_TOOL_DIAMOND, 1), 2);
    }
   } else if (above.isWood(worldIn, pos.up())) {
    changeItemTo(worldIn, pos, playerIn, new ItemStack(FeatureTool.CHAINSAW, 1), 1);
   } else if (above.equals(Blocks.STONE)) {
    changeItemTo(worldIn, pos, playerIn, new ItemStack(FeatureTool.MINING_TOOL_IRON, 1), 1);
   }
  }
  return EnumActionResult.PASS;
 }

 @SubscribeEvent
 public void onCraftHandle(PlayerInteractEvent.RightClickBlock event) {

  ItemStack heldStack = event.getEntityPlayer().getHeldItemMainhand();
  if (heldStack != null && heldStack.getItem() == Items.STICK) {
   if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.LAPIS_BLOCK && heldStack.stackSize == 1) {
    changeItemTo(event.getWorld(), event.getPos(), event.getEntityPlayer(), new ItemStack(FeatureTool.TOOL_HANDLE, 1), 0);
    event.setCanceled(true);
   }
  }
 }
}
