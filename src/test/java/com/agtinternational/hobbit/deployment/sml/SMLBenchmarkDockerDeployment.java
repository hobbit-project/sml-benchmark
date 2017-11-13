package com.agtinternational.hobbit.deployment.sml;

import com.agtinternational.hobbit.deployment.CommonConstants;
import com.agtinternational.hobbit.deployment.Dockerizer;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;

import java.io.IOException;

import static com.agtinternational.hobbit.benchmarks.sml.SMLConstants.FORMAT_RDF;

/**
 * @author Roman Katerinenko
 */
public class SMLBenchmarkDockerDeployment {
    public static void main(String[] args) throws InterruptedException, DockerException, DockerCertificateException, IOException {
        Dockerizer dockerizer = new SMLBenchmarkDockerBuilder()
                .benchmarkOutputFormat(FORMAT_RDF)
                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
                .systemUri(CommonConstants.SYSTEM_URI)
                .build();
        dockerizer.removeImagesAndContainers();
        dockerizer.buildImage();
        dockerizer.createContainer();
    }
}