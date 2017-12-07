package com.agtinternational.hobbit.sml.sml.docker;

import com.agtinternational.hobbit.sml.SMLDockersBuilder;
import com.agtinternational.hobbit.sml.SMLConstants;
import com.agtinternational.hobbit.sdk.JenaKeyValue;
import com.agtinternational.hobbit.sdk.docker.*;
import com.agtinternational.hobbit.sml.sml.SMLBenchmarkRunner;

import static org.hobbit.core.Constants.BENCHMARK_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public abstract class SMLBenchmarkDockersBuilder extends SMLDockersBuilder {
    private int benchmarkOutputFormat;

    public SMLBenchmarkDockersBuilder(String dockerizerName) {
        super(dockerizerName);
        runnerClass(SMLBenchmarkRunner.class);
        addEnvironmentVariable(BENCHMARK_PARAMETERS_MODEL_KEY, createParameters());
    }

    //@Override
    public SMLDockersBuilder parameters(String parameters) {
        addEnvironmentVariable(BENCHMARK_PARAMETERS_MODEL_KEY, parameters);
        return this;
    }


    public SMLBenchmarkDockersBuilder benchmarkOutputFormat(int benchmarkOutputFormat) {
        this.benchmarkOutputFormat = benchmarkOutputFormat;
        return this;
    }

    private String createParameters() {
        JenaKeyValue kv = new JenaKeyValue();
        kv.setValue(SMLConstants.DATA_POINT_COUNT_INPUT_NAME, SMLConstants.EXPECTED_DATA_POINTS_COUNT);
        kv.setValue(SMLConstants.PROBABILITY_THRESHOLD_INPUT_NAME, SMLConstants.PROBABILITY_THRESHOLD_DEFAULT);
        kv.setValue(SMLConstants.WINDOW_SIZE_INPUT_NAME, SMLConstants.WINDOW_SIZE_DEFAULT);
        kv.setValue(SMLConstants.TRANSITIONS_COUNT_INPUT_NAME, SMLConstants.TRANSITIONS_COUNT_DEFAULT);
        kv.setValue(SMLConstants.MAX_CLUSTER_ITERATIONS_INPUT_NAME, SMLConstants.MAX_CLUSTER_ITERATIONS_DEFAULT);
        kv.setValue(SMLConstants.INTERVAL_NANOS_INPUT_NAME, SMLConstants.INTERVAL_NANOS_DEFAULT);
        kv.setValue(SMLConstants.SEED_INPUT_NAME, SMLConstants.SEED_DEFAULT);
        kv.setValue(SMLConstants.FORMAT_INPUT_NAME, benchmarkOutputFormat);
        kv.setValue(SMLConstants.MACHINE_COUNT_INPUT_NAME, 1);
        kv.setValue(SMLConstants.TIMEOUT_MINUTES_INPUT_NAME, 1);
        kv.setValue(SMLConstants.BENCHMARK_MODE_INPUT_NAME, SMLConstants.BENCHMARK_MODE_STATIC);
        return kv.encodeToString();
    }

    @Override
    public BuildBasedDockerizer build() throws Exception {
        return super.build();
    }
}