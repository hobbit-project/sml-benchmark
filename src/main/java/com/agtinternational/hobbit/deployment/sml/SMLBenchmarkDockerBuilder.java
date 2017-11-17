package com.agtinternational.hobbit.deployment.sml;

import com.agtinternational.hobbit.sdk.docker.Dockerizer;

/**
 * @author Roman Katerinenko
 */
public class SMLBenchmarkDockerBuilder extends SMLBenchmarkDockersBuilder {
    public SMLBenchmarkDockerBuilder() {
        super("SMLBenchmarkDockerizer");
    }

    @Override
    public Dockerizer build() {
        imageName("git.project-hobbit.eu:4567/smirnp/debs17-benchmark-patched");
        containerName("cont_name_smlbenchmark");

        return super.build();
    }
}