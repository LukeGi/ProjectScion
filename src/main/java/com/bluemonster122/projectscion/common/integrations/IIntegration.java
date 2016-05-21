package com.bluemonster122.projectscion.common.integrations;

public interface IIntegration {
    void preInit();

    void init();

    void postInit();
}
