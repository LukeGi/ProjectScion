package com.bluemonster122.projectscion.common.blocks;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.util.IProvideRecipe;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class BlockObsidianCompressed extends BlockBase implements IProvideRecipe {

    public BlockObsidianCompressed() {

        super(Material.ground, "compressed_obsidian");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("compressed_obsidian");
    }

    @Override
    public void RegisterRecipes() {

        GameRegistry.addShapedRecipe(ModBlocks.COMPRESSED_OBSIDIAN.getStack(1), "OOO", "OOO", "OOO", 'O', new ItemStack(Blocks.obsidian, 1));
    }
}
