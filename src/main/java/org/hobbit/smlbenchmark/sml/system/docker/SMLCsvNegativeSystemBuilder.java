package org.hobbit.smlbenchmark.sml.system.docker;

import org.hobbit.smlbenchmark.common.docker.SMLDockersBuilder;
import org.hobbit.smlbenchmark.sml.system.SMLCsvSystemNegativeRunner;

import static org.hobbit.core.Constants.SYSTEM_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvNegativeSystemBuilder extends SMLDockersBuilder {
    public SMLCsvNegativeSystemBuilder() throws Exception {
        super("SMLCsvNegativeSystemDockerizer");
        //imageName(getImageNamePrefix()+"-systemcsvneg");
        runnerClass(SMLCsvSystemNegativeRunner.class);

        //addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, (String)System.getenv().get(SYSTEM_PARAMETERS_MODEL_KEY));
        //addEnvironmentVariable(CONTAINER_NAME_KEY, getContainerName());

    }

//    public SMLCsvNegativeSystemBuilder parameters(String value) {
//        addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, value);
//        return this;
//    }
}