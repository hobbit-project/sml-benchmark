package com.agtinternational.hobbit.sml.sml.system.docker;

import com.agtinternational.hobbit.sml.SMLDockersBuilder;
import com.agtinternational.hobbit.sml.sml.system.SMLCsvSystemNegativeRunner;

import static org.hobbit.core.Constants.SYSTEM_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvNegativeSystemBuilder extends SMLDockersBuilder {
    public SMLCsvNegativeSystemBuilder() {
        super("DebsCsvNegativeSystemDockerizer");
        imageName("git.project-hobbit.eu:4567/smirnp/debs-2017-systemcsvneg");
        containerName("cont_name_smlsystemcsvneg");
        runnerClass(SMLCsvSystemNegativeRunner.class);
    }

    public SMLCsvNegativeSystemBuilder parameters(String parameters) {
        addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, parameters);
        return this;
    }
}