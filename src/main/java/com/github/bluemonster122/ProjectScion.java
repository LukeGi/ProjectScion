package com.github.bluemonster122;

import com.github.bluemonster122.proxy.IProxy;
import com.github.bluemonster122.utils.IInitializeWithFMLEvents;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = ProjectScion.MOD_ID,
        name = ProjectScion.MOD_NAME,
        version = ProjectScion.MOD_VERSION

)
public class ProjectScion implements IInitializeWithFMLEvents {
    public static final String MOD_NAME = "Project Scion";
    public static final String MOD_ID = "projectscion";
    public static final String MOD_VERSION = "1.0.0a";
    public static final String PACKAGE_MAIN = "com.github.bluemonster122";
    public static final String PACKAGE_PROXY = PACKAGE_MAIN + ".proxy";
    public static final String CLIENT_PROXY_CLASS = PACKAGE_PROXY + ".ClientProxy";
    public static final String SERVER_PROXY_CLASS = PACKAGE_PROXY + ".ServerProxy";

    @Instance
    public static ProjectScion INSTANCE;

    @SidedProxy(
            clientSide = CLIENT_PROXY_CLASS,
            serverSide = SERVER_PROXY_CLASS,
            modId = MOD_ID
    )
    public static IProxy proxy;

    public static Logger logger;

    @EventHandler
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        proxy.preInit(event);
    }

    @EventHandler
    @Override
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
