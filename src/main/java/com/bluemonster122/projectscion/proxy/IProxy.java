package com.bluemonster122.projectscion.proxy;

import java.io.File;

public interface IProxy {
    /**
     * Register ModBlocks
     */
    void registerBlocks();

    /**
     * Register ModItems
     */
    void registerItems();

    /**
     * Register Furnace Recipes
     */
    void registerFurnaceRecipes();

    /**
     * Register Recipes
     */
    void registerRecipes();

    /**
     * Register Events
     */
    void registerEvents();

    /**
     * Register GUIs
     */
    void registerGUIs();

    /**
     * Register Renderers
     */
    void registerRenderers();

    /**
     * Register Configuration
     * @param configFile Configuration File
     */
    void registerConfiguration(File configFile);
}

