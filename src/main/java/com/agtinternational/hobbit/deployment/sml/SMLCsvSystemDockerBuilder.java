package com.agtinternational.hobbit.deployment.sml;

import com.agtinternational.hobbit.benchmarks.sml.SMLCsvSystemRunner;
import com.agtinternational.hobbit.sdk.docker.HobbitDockersBuilder;

import static org.hobbit.core.Constants.SYSTEM_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystemDockerBuilder extends SMLDockersBuilder {
    public SMLCsvSystemDockerBuilder() {
        super("SMLCsvSystemDockerizer");
        imageName("git.project-hobbit.eu:4567/smirnp/debs-2017-systemcsv");
        containerName("cont_name_smlsystemcsv");
        runnerClass(SMLCsvSystemRunner.class);
    }

    @Override
    public HobbitDockersBuilder parameters(String parameters) {
        addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, parameters);
        return this;
    }
}