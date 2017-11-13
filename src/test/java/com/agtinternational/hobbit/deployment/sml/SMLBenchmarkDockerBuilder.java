package com.agtinternational.hobbit.deployment.sml;

import com.agtinternational.hobbit.deployment.Dockerizer;

/**
 * @author Roman Katerinenko
 */
public class SMLBenchmarkDockerBuilder extends SMLBenchmarkDockersBuilder {
    public SMLBenchmarkDockerBuilder() {
        super("SMLBenchmarkDockerizer");
    }

    @Override
    public Dockerizer build() {
        imageName("git.project-hobbit.eu:4567/smirnp/sml-benchmark-1.0");
        containerName("cont_name_smlbenchmark");
        return super.build();
    }
}