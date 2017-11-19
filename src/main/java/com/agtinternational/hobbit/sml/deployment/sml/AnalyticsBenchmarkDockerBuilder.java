package com.agtinternational.hobbit.sml.deployment.sml;

import com.agtinternational.hobbit.sdk.docker.Dockerizer;

/**
 * @author Roman Katerinenko
 */
public class AnalyticsBenchmarkDockerBuilder extends SMLBenchmarkDockersBuilder {
    public AnalyticsBenchmarkDockerBuilder() {
        super("AnalyticsBenchmarkDockerizer");
    }

    @Override
    public Dockerizer build() {
        imageName("git.project-hobbit.eu:4567/rkaterinenko/analyticsbenchmark");
        containerName("cont_name_analyticsbenchmark");
        return super.build();
    }
}