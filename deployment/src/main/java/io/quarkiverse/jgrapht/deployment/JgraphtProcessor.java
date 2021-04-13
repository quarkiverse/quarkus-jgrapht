package io.quarkiverse.jgrapht.deployment;

import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;

class JgraphtProcessor {

    private static final String FEATURE = "jgrapht";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    NativeImageResourceBuildItem nativeImageResourceBuildItem() {
        return new NativeImageResourceBuildItem("graphml.xsd", "xlink.xsd", "viz.xsd", "gexf.xsd");
    }
}
