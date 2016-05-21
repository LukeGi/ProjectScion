package com.bluemonster122.projectscion.common.items;

import com.bluemonster122.projectscion.common.items.tools.ItemDiamondMiningTool;
import com.bluemonster122.projectscion.common.items.tools.ItemIronChainsaw;
import com.bluemonster122.projectscion.common.items.tools.ItemIronMiningTool;
import com.bluemonster122.projectscion.common.items.tools.ItemToolHandle;
import com.bluemonster122.projectscion.common.util.RegistrationHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public enum Items {
    TOOL_HANDLE(ItemToolHandle.class),
    IRON_MINING_TOOL(ItemIronMiningTool.class),
    DIAMOND_MINING_TOOL(ItemDiamondMiningTool.class),
    IRON_CHAINSAW(ItemIronChainsaw.class);

    private final Class<? extends Item> itemClass;
    private Item item;

    Items(Class<? extends Item> itemClass) {
        this.itemClass = itemClass;
    }

    public static void registerItems() {
        for (Items i : Items.values()) {
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
