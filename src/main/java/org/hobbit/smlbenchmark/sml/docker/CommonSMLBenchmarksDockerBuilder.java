package org.hobbit.smlbenchmark.sml.docker;

import org.hobbit.smlbenchmark.common.docker.SMLDockersBuilder;
import org.hobbit.smlbenchmark.SMLConstants;
import org.hobbit.sdk.JenaKeyValue;
import org.hobbit.sdk.docker.*;
import org.hobbit.smlbenchmark.sml.SMLBenchmarkRunner;

import static org.hobbit.core.Constants.BENCHMARK_PARAMETERS_MODEL_KEY;
import static org.hobbit.core.Constants.HOBBIT_EXPERIMENT_URI_KEY;

/**
 * @author Roman Katerinenko
 */
public abstract class CommonSMLBenchmarksDockerBuilder extends SMLDockersBuilder {


    public CommonSMLBenchmarksDockerBuilder(String dockerizerName) throws Exception {
        super(dockerizerName);
        runnerClass(SMLBenchmarkRunner.class);
        addEnvironmentVariable(HOBBIT_EXPERIMENT_URI_KEY, (String)System.getenv().get(HOBBIT_EXPERIMENT_URI_KEY));
    }

    public CommonSMLBenchmarksDockerBuilder parameters(String parameters) {
        addEnvironmentVariable(BENCHMARK_PARAMETERS_MODEL_KEY, parameters);
        return this;
    }

    @Override
    public BuildBasedDockerizer build() throws Exception {
        return super.build();
    }
}