package com.agtinternational.hobbit.deployment;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.PortBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Note! Requires Docker to be installed and 'rabbit' image to be built
 *
 * @author Roman Katerinenko
 */
public class RabbitMqDockerizer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RabbitMqDockerizer.class);

    private static final String RABBIT_MQ_IMAGE = "rabbitmq";

    private final String host;
    private final String containerName;
    private final String[] networks;

    private Dockerizer dockerizer;

    private RabbitMqDockerizer(Builder builder) {
        host = builder.host;
        containerName = builder.containerName;
        networks = builder.networks;
    }

    @Override
    public void run() {
        try {
            dockerizer = new Dockerizer.Builder("RabbitMQDockerizer")
                    .imageName(RABBIT_MQ_IMAGE)
                    .containerName(containerName)
                    .addPortBindings("5672/tcp", PortBinding.of("0.0.0.0", 5672))
                    .addNetworks(networks)
                    .build();
            dockerizer.removeAllSameNamedContainers();
            dockerizer.createContainer();
            dockerizer.startContainer();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }

    public void attachToContainerAndReadLogs() {
        dockerizer.attachToContainerAndReadLogs();
    }

    public void waitUntilRunning() throws DockerCertificateException, InterruptedException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        boolean connected = false;
        while (!connected) {
            try {
                Connection connection = factory.newConnection();
                connected = true;
                connection.close();
            } catch (IOException e) {
                // ignore
            }
            Thread.sleep(300);
        }
    }

    public void stopAndRemoveContainer() throws InterruptedException, DockerException, DockerCertificateException {
        dockerizer.removeAllSameNamedContainers();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String host;
        private String containerName;
        private String[] networks;

        private Builder() {
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder containerName(String containerName) {
            this.containerName = containerName;
            return this;
        }

        public Builder networks(String... networks) {
            this.networks = networks;
            return this;
        }

        public RabbitMqDockerizer build() {
            return new RabbitMqDockerizer(this);
        }
    }
}