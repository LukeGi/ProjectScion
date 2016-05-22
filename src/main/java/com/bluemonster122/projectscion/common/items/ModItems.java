package com.bluemonster122.projectscion.common.items;

import com.bluemonster122.projectscion.common.items.tools.*;
import com.bluemonster122.projectscion.common.util.RegistrationHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum ModItems {
    TOOL_HANDLE(ItemToolHandle.class),
    IRON_MINING_TOOL(ItemIronMiningTool.class),
    DIAMOND_MINING_TOOL(ItemDiamondMiningTool.class),
    IRON_CHAINSAW(ItemIronChainsaw.class),
    TREE_FARM_UPGRADE(ItemTreeFarmUpgrade.class),
    AREA_DESIGNATOR(ItemAreaDesignator.class),;

    private final Class<? extends Item> itemClass;
    private Item item;

    ModItems(Class<? extends Item> itemClass) {
        this.itemClass = itemClass;
    }

    public static void registerItems() {
        for (ModItems i : ModItems.values()) {
            i.registerItem();
        }
    }

    public ItemStack getStack() {
        return new ItemStack(item);
    }

    public ItemStack getStack(int size) {
        return new ItemStack(item, size);
    }

    public ItemStack getStack(int size, int damage) {
        return new ItemStack(item, size, damage);
    }

    public Item getItem() {
        return this.item;
    }

    private void registerItem() {
        item = RegistrationHelper.registerItem(itemClass);
    }
}
