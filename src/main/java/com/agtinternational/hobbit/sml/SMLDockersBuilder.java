package com.agtinternational.hobbit.sml;

import com.agtinternational.hobbit.sdk.CommonConstants;

import com.agtinternational.hobbit.sdk.docker.builders.common.DynamicDockerFileBuilder;
import org.hobbit.core.Constants;

import java.io.Reader;
import java.io.StringReader;

import static org.hobbit.core.Constants.RABBIT_MQ_HOST_NAME_KEY;

/**
 * @author Roman Katerinenko
 */
public abstract class SMLDockersBuilder extends DynamicDockerFileBuilder {

    public SMLDockersBuilder(String dockerizerName) {
        super(dockerizerName);
        repoPath("git.project-hobbit.eu:4567/smirnp");
        jarFilePath("sml-benchmark-1.0-SNAPSHOT.jar");
        buildDirectory("target");
        dockerWorkDir("/usr/src/sml-benchmark");
        addNetworks(CommonConstants.networks);
        addEnvironmentVariable(RABBIT_MQ_HOST_NAME_KEY, (String)System.getenv().get(Constants.RABBIT_MQ_HOST_NAME_KEY));
    }

    //abstract SMLDockersBuilder parameters(String parameters);

//    protected Reader getDockerFileContent() {
//        String content = String.format(
//                "FROM java\n" +
//                        "RUN mkdir -p /usr/src/sml\n" +
//                        "WORKDIR /usr/src/sml\n" +
//                        "ADD ./sml-benchmark-1.0-SNAPSHOT.jar /usr/src/sml\n" +
//                        //"ADD ./original-wm-data-gen-1.0-SNAPSHOT.jar /usr/src/sml\n" +
//                        //"CMD [\"java\", \"-Xmx250g\", \"-cp\", \"sml-benchmark-1.0-SNAPSHOT.jar:original-wm-data-gen-1.0-SNAPSHOT.jar\", \"%s\"]",
//                        "CMD [\"java\", \"-cp\", \"sml-benchmark-1.0-SNAPSHOT.jar\", \"%s\"]",
//                runnerClass.getCanonicalName());
//        return new StringReader(content);
//    }
}