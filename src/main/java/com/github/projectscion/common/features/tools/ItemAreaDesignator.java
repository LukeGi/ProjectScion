package com.github.projectscion.common.features.tools;

import com.github.projectscion.ModInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class ItemAreaDesignator extends Item {

    public ItemAreaDesignator() {

        super();
        setCreativeTab(CreativeTabs.TOOLS);
        setRegistryName(new ResourceLocation(ModInfo.MOD_ID, "area_designator"));
        setUnlocalizedName(getRegistryName().toString());
        setMaxStackSize(1);
    }


}
