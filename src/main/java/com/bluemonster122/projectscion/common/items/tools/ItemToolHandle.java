package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ModInfo;
import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.blocks.ModBlocks;
import com.bluemonster122.projectscion.common.items.ItemBase;
import com.bluemonster122.projectscion.common.items.ModItems;
import com.bluemonster122.projectscion.common.util.EnumHandleProperty;
import com.bluemonster122.projectscion.common.util.IProvideEvent;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.*;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class ItemToolHandle extends ItemBase implements IProvideEvent {

    IProperty TYPE = PropertyEnum.create("type", EnumHandleProperty.class);

    public ItemToolHandle() {

        super("toolhandle");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("toolhandle");
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    @Override
    public void getSubItems(Item id, CreativeTabs tab, List<ItemStack> list) {

        for (EnumHandleProperty type : EnumHandleProperty.values()) {
            list.add(new ItemStack(id, 1, type.ordinal()));
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerItemRenderer() {

        for (int i = 0; i < EnumHandleProperty.values().length; i++) {
            ModelLoader.setCustomModelResourceLocation(this, i, new ModelResourceLocation(String.format("%s:%s-%s", ModInfo.MOD_ID, resourcePath, EnumHandleProperty.values()[i].getName().toLowerCase()), "inventory"));
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return super.getUnlocalizedName(stack) + "." + EnumHandleProperty.values()[stack.getItemDamage()].getName();
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
                    changeItemTo(worldIn, pos, stack, ModItems.DIAMOND_MINING_TOOL.getStack(1), 2);
                    return EnumActionResult.SUCCESS;
                }
            } else {
                if (above.isWood(worldIn, pos.up())) {
                    changeItemTo(worldIn, pos, stack, ModItems.IRON_CHAINSAW.getStack(1), 1);
                    return EnumActionResult.SUCCESS;
                } else if (above == Blocks.stone) {
                    changeItemTo(worldIn, pos, stack, ModItems.IRON_MINING_TOOL.getStack(1), 1);
                    return EnumActionResult.SUCCESS;
                }
            }
            if (above == ModBlocks.UNOBTAINABLE_MATTER_BLOCK.getBlock()) {
                if (worldIn.getBlockState(pos.up().up()).getBlock() == ModBlocks.UNOBTAINABLE_MATTER_BLOCK.getBlock()) {
                    changeItemTo(worldIn, pos, stack, ModItems.UNOBTAINABLE_SWORD.getStack(1), 1);
                }
            }
        }
        if (worldIn.getBlockState(pos).getBlock() == ModBlocks.AREA_DEFINITION.getBlock()) {
            changeItemTo(worldIn, pos, stack, ModItems.AREA_DESIGNATOR.getStack(1), 0);
            return EnumActionResult.SUCCESS;
        }
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
        if (heldStack != null && heldStack.getItem() == Items.stick) {
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.lapis_block && heldStack.stackSize == 1) {
                changeItemTo(event.getWorld(), event.getPos(), heldStack, ModItems.TOOL_HANDLE.getStack(1, 0), 0);
                event.setCanceled(true);
            }
        }
        if (heldStack != null && heldStack.getItem() == ModItems.DIAMOND_ROD.getItem()) {
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == ModBlocks.COMPRESSED_OBSIDIAN.getBlock() && heldStack.stackSize == 1) {
                changeItemTo(event.getWorld(), event.getPos(), heldStack, ModItems.TOOL_HANDLE.getStack(1, 1), 0);
            }
        }
    }
}
