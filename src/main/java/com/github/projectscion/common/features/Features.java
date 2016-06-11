package com.github.projectscion.common.features;

import com.github.projectscion.common.features.cobbleGenerator.FeatureCobbleGenerator;
import com.github.projectscion.common.features.tools.FeatureTool;
import com.github.projectscion.common.features.treefarm.FeatureTreeFarm;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Features {
    static List<Feature> features = new ArrayList<Feature>();

    static {
        addFeature(new FeatureTool());
        addFeature(new FeatureTreeFarm());
        addFeature(new FeatureCobbleGenerator());
    }

    private static void addFeature(Feature feature) {
        features.add(feature);
    }

    private static void foreachfeature(Consumer<Feature> action) {
        features.forEach(action);
    }

    public static void registerCongifurations(File configFile) {
        foreachfeature((feature) -> feature.registerConfigurations(configFile));
    }

    public static void preInit() {
        foreachfeature(Feature::preInit);
    }

    public static void init() {
        foreachfeature(Feature::init);
    }

    public static void postInit() {
        foreachfeature(Feature::postInit);
    }
}
