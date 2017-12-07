package com.agtinternational.hobbit.sml.sml.docker;


import com.agtinternational.hobbit.sdk.docker.BuildBasedDockerizer;

/**
 * @author Roman Katerinenko
 */
public class AnalyticsBenchmarkDockerBuilder extends SMLBenchmarkDockersBuilder {
    public AnalyticsBenchmarkDockerBuilder() {
        super("AnalyticsBenchmarkDockerizer");
    }

    @Override
    public BuildBasedDockerizer build() throws Exception {
        imageName("git.project-hobbit.eu:4567/rkaterinenko/analyticsbenchmark");
        containerName("cont_name_analyticsbenchmark");
        return super.build();
    }
}