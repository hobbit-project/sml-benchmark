package com.agtinternational.hobbit.sml.sml.docker;


import com.agtinternational.hobbit.sdk.docker.BuildBasedDockerizer;
import com.agtinternational.hobbit.sml.sml.SMLBenchmarkRunner;

/**
 * @author Roman Katerinenko
 */
public class SMLBenchmarkDockerBuilder extends SMLBenchmarkDockersBuilder {
    public SMLBenchmarkDockerBuilder() {
        super("SMLBenchmarkDockerizer");
        imageName("debs17-benchmark-patched");
        containerName("cont_name_smlbenchmark");
        runnerClass(SMLBenchmarkRunner.class);
    }

    @Override
    public BuildBasedDockerizer build() throws Exception {

        return super.build();
    }
}