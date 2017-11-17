package com.agtinternational.hobbit.deployment.parrot;

import com.agtinternational.hobbit.benchmarks.parrot.ParrotBenchmarkRunner;
import com.agtinternational.hobbit.sdk.CommonConstants;

import com.agtinternational.hobbit.benchmarks.parrot.ParrotTask;
import com.agtinternational.hobbit.sdk.docker.*;
import com.agtinternational.hobbit.sdk.*;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import java.io.IOException;

import static org.hobbit.core.Constants.BENCHMARK_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class ParrotBenchmarkDockerDeployment extends ParrotDockersBuilder {
    public static final int MESSAGE_COUNT = 17;

    public static void main(String[] args) throws InterruptedException, DockerException, DockerCertificateException, IOException {
        Dockerizer dockerizer = new ParrotBenchmarkDockerDeployment()
                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
                .systemUri(CommonConstants.SYSTEM_URI)
                .build();
        dockerizer.removeImagesAndContainers();
        dockerizer.buildImage();
        dockerizer.createContainer();
//        dockerizer.startContainer();
//        dockerizer.attachToContainerAndReadLogs();
    }

    public ParrotBenchmarkDockerDeployment() {
        super("ParrotBenchmarkDockerizer");
        imageName("git.project-hobbit.eu:4567/rkaterinenko/debsparrotbenchmark");
        containerName("cont_name_debsparrotbenchmark");
        runnerClass(ParrotBenchmarkRunner.class);
        addEnvironmentVariable(BENCHMARK_PARAMETERS_MODEL_KEY, createParameters());
    }

    private static String createParameters() {
        JenaKeyValue parameters = new JenaKeyValue();
        parameters.setValue(ParrotTask.MESSAGE_COUNT_INPUT_NAME, MESSAGE_COUNT);
        return parameters.encodeToString();
    }
}