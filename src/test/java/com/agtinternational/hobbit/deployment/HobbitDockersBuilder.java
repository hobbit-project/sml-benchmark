package com.agtinternational.hobbit.deployment;

import java.io.Reader;

import static org.hobbit.core.Constants.*;

/**
 * @author Roman Katerinenko
 */
public abstract class HobbitDockersBuilder {
    private static final String DEFAULT_BUILD_DIRECTORY = "./src/test/resources";
    private static final String EXPERIMENT_URI = "http://example.com/exp1";

    private final Dockerizer.Builder dockerizerBuilder;

    private String imageName;
    private String containerName;
    private String systemUri;
    private String hobbitSessionId;

    public HobbitDockersBuilder(String dockerizerName) {
        dockerizerBuilder = new Dockerizer.Builder(dockerizerName);
    }


    public HobbitDockersBuilder systemUri(String systemUri) {
        this.systemUri = systemUri;
        return this;
    }

    public HobbitDockersBuilder hobbitSessionId(String hobbitSessionId) {
        this.hobbitSessionId = hobbitSessionId;
        return this;
    }

    public HobbitDockersBuilder imageName(String imageName) {
        this.imageName = imageName;
        return this;
    }

    public HobbitDockersBuilder containerName(String containerName) {
        this.containerName = containerName;
        return this;
    }

    public HobbitDockersBuilder addEnvironmentVariable(String key, String value) {
        dockerizerBuilder.addEnvironmentVariable(key, value);
        return this;
    }

    public Dockerizer build() {
        dockerizerBuilder.tempDirectory(DEFAULT_BUILD_DIRECTORY)
                .imageName(imageName)
                .containerName(containerName)
                .dockerFileReader(getDockerFileContent())
                .addEnvironmentVariable(RABBIT_MQ_HOST_NAME_KEY, CommonConstants.RABBIT_MQ_HOST_NAME)
                .addEnvironmentVariable(HOBBIT_SESSION_ID_KEY, hobbitSessionId)
                .addEnvironmentVariable(HOBBIT_EXPERIMENT_URI_KEY, EXPERIMENT_URI)
                .addEnvironmentVariable(SYSTEM_URI_KEY, systemUri)
                .addNetworks(CommonConstants.HOBBIT_NETWORK_NAME)
                .addNetworks(CommonConstants.HOBBIT_CORE_NETWORK_NAME);
        Dockerizer dockerizer = dockerizerBuilder.build();
        if (dockerizer == null) {
            throw new IllegalStateException("Unable to dockerize system: ");
        }
        return dockerizer;
    }

    protected abstract Reader getDockerFileContent();
}