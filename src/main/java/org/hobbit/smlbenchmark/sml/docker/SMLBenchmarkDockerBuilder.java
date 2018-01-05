package org.hobbit.smlbenchmark.sml.docker;


import org.hobbit.sdk.docker.BuildBasedDockerizer;
import org.hobbit.smlbenchmark.sml.SMLBenchmarkRunner;

/**
 * @author Roman Katerinenko
 */
public class SMLBenchmarkDockerBuilder extends CommonSMLBenchmarksDockerBuilder {
    public SMLBenchmarkDockerBuilder(String imageName) throws Exception {
        super("SMLBenchmarkDockerizer");
        imageName(imageName);
        runnerClass(SMLBenchmarkRunner.class);
    }

    @Override
    public BuildBasedDockerizer build() throws Exception {

        return super.build();
    }
}