package com.bluemonster122.projectscion.proxy;

import com.bluemonster122.projectscion.ProjectScion;
import com.bluemonster122.projectscion.common.blocks.ModBlocks;
import com.bluemonster122.projectscion.common.config.Config;
import com.bluemonster122.projectscion.common.items.ModItems;
import com.bluemonster122.projectscion.common.util.IProvideEvent;
import com.bluemonster122.projectscion.common.util.IProvideRecipe;
import com.bluemonster122.projectscion.common.util.IProvideSmelting;
import com.bluemonster122.projectscion.client.gui.GuiHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.io.File;

public abstract class CommonProxy implements IProxy {
    @Override
    public void registerBlocks() {
        ModBlocks.registerBlocks();
    }

    @Override
    public void registerItems() {
        ModItems.registerItems();
    }

    @Override
    public void registerFurnaceRecipes() {
        for (ModItems item : ModItems.values()) {
            if (item.getItem() instanceof IProvideSmelting)
                ((IProvideSmelting) item.getItem()).RegisterSmelting();
        }

        for (ModBlocks block : ModBlocks.values()) {
            if (block.getBlock() instanceof IProvideSmelting)
                ((IProvideSmelting) block.getBlock()).RegisterSmelting();
        }
    }

    @Override
    public void registerRecipes() {
        for (ModItems item : ModItems.values()) {
            if (item.getItem() instanceof IProvideRecipe)
                ((IProvideRecipe) item.getItem()).RegisterRecipes();
        }

        for (ModBlocks block : ModBlocks.values()) {
            if (block.getBlock() instanceof IProvideRecipe)
                ((IProvideRecipe) block.getBlock()).RegisterRecipes();
        }
    }

    @Override
    public void registerEvents() {
        for (ModItems item : ModItems.values()) {
            if (item.getItem() instanceof IProvideEvent)
                MinecraftForge.EVENT_BUS.register(item.getItem());
        }

        for (ModBlocks block : ModBlocks.values()) {
            if (block.getBlock() instanceof IProvideEvent)
                MinecraftForge.EVENT_BUS.register(block.getBlock());
        }
    }

    @Override
    public void registerGUIs() {
        NetworkRegistry.INSTANCE.registerGuiHandler(ProjectScion.instance, new GuiHandler());
    }

    @Override
    public void registerRenderers() {
        /** Client Side Only **/
    }

    @Override
    public void registerConfiguration(File configFile) {
        ProjectScion.configuration = Config.initConfig(configFile);
    }
}
