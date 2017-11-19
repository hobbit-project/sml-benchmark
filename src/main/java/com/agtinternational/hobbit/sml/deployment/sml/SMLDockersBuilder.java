package com.agtinternational.hobbit.sml.deployment.sml;

import com.agtinternational.hobbit.sdk.docker.HobbitDockersBuilder;

import java.io.Reader;
import java.io.StringReader;

/**
 * @author Roman Katerinenko
 */
public abstract class SMLDockersBuilder extends HobbitDockersBuilder {
    private Class runnerClass;

    public SMLDockersBuilder(String dockerizerName) {
        super(dockerizerName);

    }

    HobbitDockersBuilder runnerClass(Class runnerClass) {
        this.runnerClass = runnerClass;
        return this;
    }

    abstract HobbitDockersBuilder parameters(String parameters);

    protected Reader getDockerFileContent() {
        String content = String.format(
                "FROM java\n" +
                        "RUN mkdir -p /usr/src/sml\n" +
                        "WORKDIR /usr/src/sml\n" +
                        "ADD ./sml-benchmark-1.0-SNAPSHOT.jar /usr/src/sml\n" +
                        //"ADD ./original-wm-data-gen-1.0-SNAPSHOT.jar /usr/src/sml\n" +
                        //"CMD [\"java\", \"-Xmx250g\", \"-cp\", \"sml-benchmark-1.0-SNAPSHOT.jar:original-wm-data-gen-1.0-SNAPSHOT.jar\", \"%s\"]",
                        "CMD [\"java\", \"-Xmx250g\", \"-cp\", \"sml-benchmark-1.0-SNAPSHOT.jar\", \"%s\"]",
                runnerClass.getCanonicalName());
        return new StringReader(content);
    }
}