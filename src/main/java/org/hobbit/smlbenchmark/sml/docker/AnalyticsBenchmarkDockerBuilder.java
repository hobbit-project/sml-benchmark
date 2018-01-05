package org.hobbit.smlbenchmark.sml.docker;


import org.hobbit.sdk.docker.BuildBasedDockerizer;

/**
 * @author Roman Katerinenko
 */
public class AnalyticsBenchmarkDockerBuilder extends CommonSMLBenchmarksDockerBuilder {
    public AnalyticsBenchmarkDockerBuilder() throws Exception {
        super("AnalyticsBenchmarkDockerizer");
    }

    @Override
    public BuildBasedDockerizer build() throws Exception {
        imageName("git.project-hobbit.eu:4567/rkaterinenko/analyticsbenchmark");
        containerName("cont_name_analyticsbenchmark");
        return super.build();
    }
}