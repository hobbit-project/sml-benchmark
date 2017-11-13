package com.agtinternational.hobbit.deployment.sml;

import com.agtinternational.hobbit.common.JenaKeyValue;
import com.agtinternational.hobbit.benchmarks.sml.SMLBenchmarkRunner;
import com.agtinternational.hobbit.benchmarks.sml.SMLConstants;
import com.agtinternational.hobbit.deployment.Dockerizer;
import com.agtinternational.hobbit.deployment.HobbitDockersBuilder;

import static org.hobbit.core.Constants.BENCHMARK_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public abstract class SMLBenchmarkDockersBuilder extends SMLDockersBuilder {
    private int benchmarkOutputFormat;

    SMLBenchmarkDockersBuilder(String dockerizerName) {
        super(dockerizerName);
    }

    @Override
    HobbitDockersBuilder parameters(String parameters) {
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
        kv.setValue(SMLConstants.TIMEOUT_MINUTES_INPUT_NAME, SMLConstants.NO_TIMEOUT);
        kv.setValue(SMLConstants.BENCHMARK_MODE_INPUT_NAME, SMLConstants.BENCHMARK_MODE_STATIC);
        return kv.encodeToString();
    }

    @Override
    public Dockerizer build() {
        runnerClass(SMLBenchmarkRunner.class);
        addEnvironmentVariable(BENCHMARK_PARAMETERS_MODEL_KEY, createParameters());
        return super.build();
    }
}