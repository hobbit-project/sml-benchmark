package com.agtinternational.hobbit;

import com.agtinternational.hobbit.sdk.CommonConstants;
import com.agtinternational.hobbit.sdk.docker.*;
import com.agtinternational.hobbit.deployment.sml.SMLBenchmarkDockerBuilder;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import org.junit.Test;

import java.io.IOException;

import static com.agtinternational.hobbit.common.SMLConstants.FORMAT_RDF;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SMLDockerizerTest {

    @Test
    public void checkBuildImage() throws InterruptedException, DockerException, DockerCertificateException, IOException {
        //Dockerizer dockerizer = new Dockerizer.Builder("TestDockerizer")

        Dockerizer dockerizer = new SMLBenchmarkDockerBuilder()
                .benchmarkOutputFormat(FORMAT_RDF)
                .hobbitSessionId(CommonConstants.HOBBIT_SESSION_ID)
                .systemUri(CommonConstants.SYSTEM_URI)
                .build();
        dockerizer.removeImagesAndContainers();
        dockerizer.buildImage();
        //dockerizer.createContainer();

//        assertTrue(isImageExist(TEST_IMAGE_NAME));
//        assertNull(dockerizer.anyExceptions());
//        assertTrue(isEnvironmentVariableExist(CONTAINER_NAME, ENV1_KEY, ENV1_VALUE));
//        assertTrue(isEnvironmentVariableExist(CONTAINER_NAME, ENV2_KEY, ENV2_VALUE));
//        assertTrue(isConnectedToNetworks(CONTAINER_NAME, NETWORK1, NETWORK2));
//        assertTrue(checkPortBindings(CONTAINER_NAME, HOST_PORT, portBinding));
        dockerizer.removeImagesAndContainers();
    }
}
