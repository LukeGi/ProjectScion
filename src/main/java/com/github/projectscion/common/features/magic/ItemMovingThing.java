package com.github.projectscion.common.features.magic;

import com.github.projectscion.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * Created by blue on 03/07/16.
 */
public class ItemMovingThing extends Item {
    public ItemMovingThing() {
        setRegistryName(ModInfo.MOD_ID, "moving_thing");
        setUnlocalizedName(getRegistryName().getResourcePath());
        setCreativeTab(CreativeTabs.MISC);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {

        return super.onItemRightClick(itemStackIn, worldIn, playerIn, hand);
    }
}
