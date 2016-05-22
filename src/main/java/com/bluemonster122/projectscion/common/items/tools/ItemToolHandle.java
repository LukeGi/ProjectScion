package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.blocks.ModBlocks;
import com.bluemonster122.projectscion.common.items.ItemBase;
import com.bluemonster122.projectscion.common.items.ModItems;
import com.bluemonster122.projectscion.common.util.IProvideEvent;
import com.bluemonster122.projectscion.common.util.IProvideRecipe;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class ItemToolHandle extends ItemBase implements IProvideEvent {

    public ItemToolHandle() {

        super("toolhandle");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("toolhandle");
        setMaxStackSize(1);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        if (worldIn.getBlockState(pos).getBlock() == Blocks.iron_block) {
            Block above = worldIn.getBlockState(pos.up()).getBlock();
            if (above == Blocks.diamond_block) {
                above = worldIn.getBlockState(pos.up(2)).getBlock();
                /*if (above.isWood(worldIn, pos.up(2))) {
                    stack.setItem(ModItems.IRON_CHAINSAW.getItem());
                    return EnumActionResult.SUCCESS;
                } else*/
                if (above == Blocks.stone) {
                    changeItemTo(worldIn, pos, stack, ModItems.DIAMOND_MINING_TOOL.getItem(), 2);
                    return EnumActionResult.SUCCESS;
                }
            } else {
                if (above.isWood(worldIn, pos.up())) {
                    changeItemTo(worldIn, pos, stack, ModItems.IRON_CHAINSAW.getItem(), 1);
                    return EnumActionResult.SUCCESS;
                } else if (above == Blocks.stone) {
                    changeItemTo(worldIn, pos, stack, ModItems.IRON_MINING_TOOL.getItem(), 1);
                    return EnumActionResult.SUCCESS;
                }
            }
        }
        if (worldIn.getBlockState(pos).getBlock() == ModBlocks.AREA_DEFINITION.getBlock()) {
            changeItemTo(worldIn, pos, stack, ModItems.AREA_DESIGNATOR.getItem(), 0);
            return EnumActionResult.SUCCESS;
        }
        return EnumActionResult.PASS;
    }

    private void changeItemTo(World worldIn, BlockPos pos, ItemStack stack, Item item, int i) {

        stack.setItem(item);
        worldIn.destroyBlock(pos, false);
        if (i >= 1) {
            worldIn.destroyBlock(pos.up(), false);
        }
        if (i >= 2) {
            worldIn.destroyBlock(pos.up(2), false);
        }
        for (int j = 0; j < 5 * i + 5; j++) {
            worldIn.weatherEffects.add(new EntityLightningBolt(worldIn, pos.getX() + worldIn.rand.nextDouble(), pos.getY(), pos.getZ() + worldIn.rand.nextDouble(), true));
        }
    }

    @SubscribeEvent
    public void onCraftHandle(PlayerInteractEvent.RightClickBlock event) {

        ItemStack heldStack = event.getEntityPlayer().getHeldItemMainhand();
        if (heldStack != null && heldStack.getItem() == Items.stick) {
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.lapis_block && heldStack.stackSize == 1) {
                changeItemTo(event.getWorld(), event.getPos(), heldStack, ModItems.TOOL_HANDLE.getItem(), 0);
                event.setCanceled(true);
            }
        }
    }
}