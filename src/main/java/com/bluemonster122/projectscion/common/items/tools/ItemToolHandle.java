package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.items.ItemBase;
import com.bluemonster122.projectscion.common.util.IProvideRecipe;
import net.minecraft.init.*;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class ItemToolHandle extends ItemBase implements IProvideRecipe {

    public ItemToolHandle() {

        super("toolhandle");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("toolhandle");
    }

    @Override
    public void RegisterRecipes() {

        GameRegistry.addShapedRecipe(com.bluemonster122.projectscion.common.items.Items.TOOL_HANDLE.getStack(1), " I ", "ILI", " I ", 'I', new ItemStack(Items.iron_ingot, 1), 'L', new ItemStack(Items.dye, 1, EnumDyeColor.BLUE.getDyeDamage()));
    }
}
