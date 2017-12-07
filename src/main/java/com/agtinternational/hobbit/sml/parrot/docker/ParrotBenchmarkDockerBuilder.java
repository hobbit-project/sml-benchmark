package com.agtinternational.hobbit.sml.parrot.docker;

import com.agtinternational.hobbit.sdk.JenaKeyValue;
import com.agtinternational.hobbit.sml.SMLDockersBuilder;
import com.agtinternational.hobbit.sml.parrot.ParrotTask;

import static org.hobbit.core.Constants.BENCHMARK_PARAMETERS_MODEL_KEY;

public class ParrotBenchmarkDockerBuilder extends SMLDockersBuilder {

    public ParrotBenchmarkDockerBuilder() {
        super("ParrotBenchmarkDockerizer");
        imageName("git.project-hobbit.eu:4567/rkaterinenko/debsparrotbenchmark");
        containerName("cont_name_debsparrotbenchmark");
        runnerClass(ParrotBenchmarkRunner.class);
        addEnvironmentVariable(BENCHMARK_PARAMETERS_MODEL_KEY, createParameters());
    }

    private static String createParameters() {
        JenaKeyValue parameters = new JenaKeyValue();
        parameters.setValue(ParrotTask.MESSAGE_COUNT_INPUT_NAME, ParrotTask.MESSAGE_COUNT);
        return parameters.encodeToString();
    }
}
