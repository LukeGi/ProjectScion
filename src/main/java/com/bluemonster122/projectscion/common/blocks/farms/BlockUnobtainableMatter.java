package com.bluemonster122.projectscion.common.blocks.farms;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.blocks.BlockBase;
import com.bluemonster122.projectscion.common.blocks.ModBlocks;
import com.bluemonster122.projectscion.common.items.ModItems;
import com.bluemonster122.projectscion.common.util.IProvideRecipe;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class BlockUnobtainableMatter extends BlockBase implements IProvideRecipe {

    public BlockUnobtainableMatter() {

        super(Material.dragonEgg, "unobtainable_matter_block");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setRegistryName("unobtainable_matter_block");
    }

    @Override
    public void RegisterRecipes() {

        GameRegistry.addShapedRecipe(ModBlocks.UNOBTAINABLE_MATTER_BLOCK.getStack(1), "UUU", "UUU", "UUU", 'U', ModItems.UNOBTAINABLE_MATTER.getStack(1));
    }
}
