package com.agtinternational.hobbit.sml.sml.system.docker;

import com.agtinternational.hobbit.sml.SMLDockersBuilder;
import com.agtinternational.hobbit.sml.sml.system.SMLCsvSystemRunner;

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

    //@Override
    public SMLDockersBuilder parameters(String parameters) {
        addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, parameters);
        return this;
    }
}