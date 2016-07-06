package com.github.projectscion.common.features.randomthings;

import com.github.projectscion.common.features.Feature;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by blue on 23/06/16.
 */
public class FeatureRandomThings extends Feature {

    public static final Item dough = new ItemRandomFood("dough", 1, 1, false);
    public static final Item pizza_base = new ItemRandomFood("pizza_base", 2, 1, false);
    public static final Item cheese = new ItemRandomFood("cheese", 2, 2, false);
    public static final Item pepperoni = new ItemRandomFood("pepperoni", 1, 1, true);
    public static final Item pepperoni_pizza = new ItemRandomFood("pepperoni_pizza", 8, 2, true);

    @Override
    public void preInit() {
        registerFeature(dough);
        registerFeature(pizza_base);
        registerFeature(cheese);
        registerFeature(pepperoni_pizza);
    }

    @Override
    public void init() {
        GameRegistry.addShapedRecipe(
                new ItemStack(dough, 5),
                "ese",
                "www",
                "eme",
                'e', new ItemStack(Items.EGG, 1),
                's', new ItemStack(Items.SUGAR, 1),
                'w', new ItemStack(Items.WHEAT, 1),
                'm', new ItemStack(Items.MILK_BUCKET, 1)
        );
        GameRegistry.addShapelessRecipe(
                new ItemStack(pepperoni, 8),
                new ItemStack(Items.BLAZE_ROD, 1),
                new ItemStack(Items.DIAMOND.setContainerItem(Items.DIAMOND), 1),
                new ItemStack(Items.DYE, 1, EnumDyeColor.RED.getDyeDamage()),
                new ItemStack(Items.COOKED_PORKCHOP, 1)
        );
        GameRegistry.addShapelessRecipe(
                new ItemStack(cheese, 1),
                new ItemStack(Items.MILK_BUCKET, 1)
        );
        GameRegistry.addShapedRecipe(
                new ItemStack(pepperoni_pizza, 1),
                "PCP",
                "PTP",
                "BBB",
                'P', new ItemStack(pepperoni, 1),
                'C', new ItemStack(cheese, 1),
                'T', new ItemStack(Items.BEETROOT_SOUP.setContainerItem(Items.BOWL), 1),
                'B', new ItemStack(pizza_base, 1)
        );
        GameRegistry.addSmelting(new ItemStack(dough, 1), new ItemStack(pizza_base, 4), 1F);
    }
}
