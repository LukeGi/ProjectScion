package com.bluemonster122.projectscion.common.blocks;

import com.bluemonster122.projectscion.common.blocks.farms.BlockFarm;
import com.bluemonster122.projectscion.common.blocks.farms.BlockTreeFarm;
import com.bluemonster122.projectscion.common.util.RegistrationHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public enum ModBlocks {
    FARM(BlockFarm.class),
    TREE_FARM(BlockTreeFarm.class),
    AREA_DEFINITION(BlockAreaDefinition.class),
    ;

    private final Class<? extends BlockBase> blockClass;
    private final Class<? extends ItemBlock> itemBlockClass;
    private Block block;

    ModBlocks(Class<? extends BlockBase> blockClass) {
        this(blockClass, ItemBlock.class);
    }

    ModBlocks(Class<? extends BlockBase> blockClass, Class<? extends ItemBlock> itemBlockClass) {
        this.blockClass = blockClass;
        this.itemBlockClass = itemBlockClass;
    }

    public static void registerBlocks() {
        for (ModBlocks block : ModBlocks.values()) {
            block.registerBlock();
        }
    }

    public ItemStack getStack() {
        return new ItemStack(block);
    }

    public ItemStack getStack(int size) {
        return new ItemStack(block, size);
    }

    public ItemStack getStack(int size, int meta) {
        return new ItemStack(block, size, meta);
    }

    public Block getBlock() {
        return this.block;
    }

    private void registerBlock() {
        block = RegistrationHelper.registerBlock(blockClass, itemBlockClass);
    }
}