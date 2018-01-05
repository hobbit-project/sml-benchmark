package org.hobbit.smlbenchmark.sml.system.docker;

import org.hobbit.smlbenchmark.common.docker.SMLDockersBuilder;
import org.hobbit.smlbenchmark.sml.system.SMLCsvSystemRunner;

import static org.hobbit.core.Constants.*;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystemDockerBuilder extends SMLDockersBuilder {
    public SMLCsvSystemDockerBuilder(String imageName) throws Exception {
        super("SMLCsvSystemDockerizer");
        imageName(imageName);
        runnerClass(SMLCsvSystemRunner.class);

        //addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, (String)System.getenv().get(SYSTEM_PARAMETERS_MODEL_KEY));
        //addEnvironmentVariable(CONTAINER_NAME_KEY, getContainerName());
    }

    //@Override
//    public SMLDockersBuilder parameters(String value) {
//
//        addEnvironmentVariable(SYSTEM_PARAMETERS_MODEL_KEY, value);
//
//        return this;
//    }
}