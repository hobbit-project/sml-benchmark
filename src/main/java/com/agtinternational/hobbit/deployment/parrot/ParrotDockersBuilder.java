package com.agtinternational.hobbit.deployment.parrot;

import com.agtinternational.hobbit.sdk.docker.*;

import java.io.Reader;
import java.io.StringReader;

/**
 * @author Roman Katerinenko
 */
abstract class ParrotDockersBuilder extends HobbitDockersBuilder {
    private Class runnerClass;

    ParrotDockersBuilder(String dockerizerName) {
        super(dockerizerName);
    }

    HobbitDockersBuilder runnerClass(Class runnerClass) {
        this.runnerClass = runnerClass;
        return this;
    }

    protected Reader getDockerFileContent() {
        String content = String.format(
                "FROM java\n" +
                        "RUN mkdir -p /usr/src/parrot\n" +
                        "WORKDIR /usr/src/parrot\n" +
                        "ADD ./sml-benchmark-1.0-SNAPSHOT.jar /usr/src/parrot\n" +
                        "CMD [\"java\", \"-cp\", \"sml-benchmark-1.0-SNAPSHOT.jar\", \"%s\"]",
                runnerClass.getCanonicalName());
        return new StringReader(content);
    }
}