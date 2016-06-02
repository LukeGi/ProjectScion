package com.github.projectscion.common.features.tools;

import com.github.projectscion.common.util.LogHelper;
import com.github.projectscion.common.features.tools.FeatureTool;
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

public class ItemToolHandle extends Item {
	public ItemToolHandle() {

		super();
		setCreativeTab(CreativeTabs.TOOLS);
		setRegistryName("tool_handle");
		setMaxStackSize(1);
		setHasSubtypes(true);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		if (worldIn.getBlockState(pos).getBlock() == Blocks.IRON_BLOCK) {
			Block above = worldIn.getBlockState(pos.up()).getBlock();
			if (above == Blocks.DIAMOND_BLOCK) {
				above = worldIn.getBlockState(pos.up(2)).getBlock();
				/*
				 * if (above.isWood(worldIn, pos.up(2))) {
				 * stack.setItem(ModItems.IRON_CHAINSAW.getItem()); return
				 * EnumActionResult.SUCCESS; } else
				 */
				// if (above == Blocks.STONE) {
				// changeItemTo(worldIn, pos, stack,
				// ModItems.DIAMOND_MINING_TOOL.getStack(1), 2);
				// return EnumActionResult.SUCCESS;
				// }TODO : add this again
			} else {

				if (above.isWood(worldIn, pos.up())) {
					changeItemTo(worldIn, pos, stack, new ItemStack(FeatureTool.chainsaw_iron, 1), 1);
					return EnumActionResult.SUCCESS;
				} else if (above == Blocks.STONE) {
					changeItemTo(worldIn, pos, stack, new ItemStack(FeatureTool.mining_tool_iron, 1), 1);
					return EnumActionResult.SUCCESS;
				}
			}
			// if (above == ModBlocks.UNOBTAINABLE_MATTER_BLOCK.getBlock()) {
			// if (worldIn.getBlockState(pos.up().up()).getBlock() ==
			// ModBlocks.UNOBTAINABLE_MATTER_BLOCK.getBlock()) {
			// changeItemTo(worldIn, pos, stack,
			// ModItems.UNOBTAINABLE_SWORD.getStack(1), 1);
			// }
			// } TODO: add this again
		}
		// if (worldIn.getBlockState(pos).getBlock() ==
		// ModBlocks.AREA_DEFINITION.getBlock()) {
		// changeItemTo(worldIn, pos, stack,
		// ModItems.AREA_DESIGNATOR.getStack(1), 0);
		// return EnumActionResult.SUCCESS;
		// } TODO: add this again
		return EnumActionResult.PASS;
	}

	public static void changeItemTo(World worldIn, BlockPos pos, ItemStack stack, ItemStack item, int i) {

		stack.setItem(item.getItem());
		stack.stackSize = item.stackSize;
		worldIn.destroyBlock(pos, false);
		if (i >= 1) {
			worldIn.destroyBlock(pos.up(), false);
		}
		if (i >= 2) {
			worldIn.destroyBlock(pos.up(2), false);
		}
		for (int j = 0; j < (5 * i) + 5; j++) {
			worldIn.weatherEffects.add(new EntityLightningBolt(worldIn, pos.getX(), pos.getY(), pos.getZ(), true));
		}
	}

	@SubscribeEvent
	public void onCraftHandle(PlayerInteractEvent.RightClickBlock event) {

		ItemStack heldStack = event.getEntityPlayer().getHeldItemMainhand();
		if (heldStack != null && heldStack.getItem() == Items.STICK) {
			if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.LAPIS_BLOCK
					&& heldStack.stackSize == 1) {
				changeItemTo(event.getWorld(), event.getPos(), heldStack, new ItemStack(FeatureTool.tool_handle, 1), 0);
				event.setCanceled(true);
			}
		}
		// if (heldStack != null && heldStack.getItem() ==
		// ModItems.DIAMOND_ROD.getItem()) {
		// if (event.getWorld().getBlockState(event.getPos()).getBlock() ==
		// ModBlocks.COMPRESSED_OBSIDIAN.getBlock()
		// && heldStack.stackSize == 1) {
		// changeItemTo(event.getWorld(), event.getPos(), heldStack,
		// ModItems.TOOL_HANDLE.getStack(1, 1), 0);
		// }
		// } TODO: add this again
	}
}
