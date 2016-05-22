package com.bluemonster122.projectscion.common.blocks.farms;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.blocks.BlockBase;
import net.minecraft.block.material.Material;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class BlockFarm extends BlockBase{

    public BlockFarm() {

        super(Material.iron, "blank_farm");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("blank_farm");
    }
}
