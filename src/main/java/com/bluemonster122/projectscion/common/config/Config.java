package com.bluemonster122.projectscion.common.config;

import com.bluemonster122.projectscion.ModInfo;
import com.bluemonster122.projectscion.ProjectScion;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.io.File;
import java.util.Arrays;

public class Config extends GuiConfig {
    public static Configuration configuration;

    public Config(GuiScreen parentScreen) {
        super(
                parentScreen,
                Arrays.asList(new IConfigElement[]{
                        new ConfigElement(ProjectScion.configuration.getCategory("SampleData"))
                }),
                ModInfo.MOD_ID, false, false, ModInfo.MOD_NAME + " Configuration");
        titleLine2 = ProjectScion.configuration.getConfigFile().getAbsolutePath();
    }

    public static Configuration initConfig(File configFile) {
        if (configuration == null) {
            configuration = new Configuration(configFile);
            loadConfiguration();
        }
        return configuration;
    }

    public static void loadConfiguration() {
        ConfigSample.init(configuration);

        configuration.save();
    }
}