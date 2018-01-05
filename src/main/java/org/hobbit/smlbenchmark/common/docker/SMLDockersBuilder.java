package org.hobbit.smlbenchmark.common.docker;

import org.hobbit.sdk.docker.builders.common.DynamicDockerFileBuilder;

import static org.hobbit.core.Constants.HOBBIT_SESSION_ID_KEY;
import static org.hobbit.core.Constants.RABBIT_MQ_HOST_NAME_KEY;
import static org.hobbit.sdk.CommonConstants.HOBBIT_NETWORKS;

/**
 * @author Roman Katerinenko
 */
public abstract class SMLDockersBuilder extends DynamicDockerFileBuilder {

    public static String GIT_REPO_PATH = "git.project-hobbit.eu:4567/smirnp/";
    public static String PROJECT_NAME = "sml-v1/";

    public static final String BENCHMARK_IMAGE_NAME = GIT_REPO_PATH+ PROJECT_NAME +"benchmark-controller";
    public static final String SYSTEM_IMAGE_NAME = GIT_REPO_PATH+ PROJECT_NAME +"system-csv";

    public SMLDockersBuilder(String dockerizerName) throws Exception {
        super(dockerizerName);
        jarFileName("sml-benchmark-1.0.1.jar");
        buildDirectory("target");
        dockerWorkDir("/usr/src/"+ PROJECT_NAME);

//        addEnvironmentVariable(RABBIT_MQ_HOST_NAME_KEY, (String)System.getenv().get(RABBIT_MQ_HOST_NAME_KEY));
//        addEnvironmentVariable(HOBBIT_SESSION_ID_KEY, (String)System.getenv().get(HOBBIT_SESSION_ID_KEY));
//        addNetworks(HOBBIT_NETWORKS);

    }

}