package com.agtinternational.hobbit.sml;

import com.agtinternational.hobbit.sml.parrot.docker.ParrotBenchmarkDockerBuilder;
import com.agtinternational.hobbit.sml.parrot.system.docker.ParrotNegativeSystemDockerBuilder;
import com.agtinternational.hobbit.sml.parrot.system.docker.ParrotNegativeSystemRunner;
import com.agtinternational.hobbit.sml.parrot.ParrotTask;


import com.agtinternational.hobbit.sdk.ComponentsExecutor;
import com.agtinternational.hobbit.sdk.docker.*;
import com.agtinternational.hobbit.sdk.utils.CommandQueueListener;
import com.agtinternational.hobbit.sdk.*;
import com.agtinternational.hobbit.sdk.utils.commandreactions.StartBenchmarkWhenSystemAndBenchmarkReady;
import com.agtinternational.hobbit.sml.parrot.system.docker.ParrotSystemDockerBuilder;
import com.spotify.docker.client.exceptions.ContainerNotFoundException;
import org.hobbit.core.Commands;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * @author Roman Katerinenko
 */
//todo check exactly one or less hobbit and hobbit-common networks exist
@RunWith(Parameterized.class)
public class DockerizedParrotBenchmarkTest extends EnvironmentVariables {
    private static final Logger logger = LoggerFactory.getLogger(DockerizedParrotBenchmarkTest.class);
    private static final String SESSION_ID = "http://example.com/sessionId1";
    private static final String FAKE_SYSTEM_URI = "http://example.com/fakeSystemId";
    private static final String RABBIT_HOST = "127.0.0.1";

    private enum SystemType {
        POSITIVE,
        NEGATIVE
    }

    private AtomicBoolean correct = new AtomicBoolean(false);
    private SystemType systemType;
    private int expectedMessagesCount;

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {
        return Arrays.asList(new Object[][]{
                {SystemType.POSITIVE, ParrotTask.MESSAGE_COUNT},
                {SystemType.NEGATIVE, ParrotNegativeSystemRunner.CHECKED_MESSAGES_COUNT_BEFORE_FAIL}
        });
    }

    public DockerizedParrotBenchmarkTest(SystemType systemType, int expectedMessagesCount) {
        this.systemType = systemType;
        this.expectedMessagesCount = expectedMessagesCount;
    }

    @Test
    public void checkBenchmarkResult() throws Exception {
        // todo add check that rabbit docker image exists and stop its container before test
        RabbitMqDockerizer rabbitMqDockerizer = RabbitMqDockerizer.builder().build();
        rabbitMqDockerizer.run();
        rabbitMqDockerizer.waitUntilRunning();
        setupCommunicationEnvironmentVariables(RABBIT_HOST, SESSION_ID);
        ComponentsExecutor componentsExecutor = new ComponentsExecutor();
        CommandQueueListener commandQueueListener = new CommandQueueListener();
        CountDownLatch benchmarkFinishedBarrier = new CountDownLatch(1);
        commandQueueListener.setCommandReactions(
                new StartBenchmarkWhenSystemAndBenchmarkReady(FAKE_SYSTEM_URI),
                (command, modelBytes) -> {
                    if (command == Commands.BENCHMARK_FINISHED_SIGNAL) {
                        correct.set(DockerizedParrotBenchmarkTest.this.checkReceivedModel(modelBytes));
                        benchmarkFinishedBarrier.countDown();
                    }
                });
        componentsExecutor.submit(commandQueueListener);
        commandQueueListener.waitForInitialisation();
        AbstractDockerizer benchmarkDockerizer = new ParrotBenchmarkDockerBuilder()
                //.hobbitSessionId(SESSION_ID)
                //.systemUri(FAKE_SYSTEM_URI)
                .build();
        componentsExecutor.submit(benchmarkDockerizer);

        AbstractDockerizer systemDockerizer = createSystemDockerizer();
        componentsExecutor.submit(systemDockerizer);
        benchmarkFinishedBarrier.await();
        try {
            benchmarkDockerizer.waitForContainerFinish();
            systemDockerizer.waitForContainerFinish();
        } catch (ContainerNotFoundException e) {
            // ignore - container already finished
        }
        commandQueueListener.terminate();
        componentsExecutor.shutdown();
        rabbitMqDockerizer.stop();
        Assert.assertTrue(correct.get());
        Assert.assertFalse(componentsExecutor.anyExceptions());
    }

    private AbstractDockerizer createSystemDockerizer() throws Exception {
        if (systemType == SystemType.POSITIVE) {

            return new ParrotSystemDockerBuilder()
                    .parameters("{}")
                    //.hobbitSessionId(SESSION_ID)
                    //.systemUri(FAKE_SYSTEM_URI)
                    .build();
        } else {
            return new ParrotNegativeSystemDockerBuilder()
                    .parameters("{}")
                    //.hobbitSessionId(SESSION_ID)
                    //.systemUri(FAKE_SYSTEM_URI)
                    .build();
        }
    }

    private boolean checkReceivedModel(byte[] bytes) {
        JenaKeyValue benchmarkResult = new JenaKeyValue.Builder().buildFrom(bytes);
        String correctnessKPI = benchmarkResult.getStringValueFor(ParrotTask.CORRECTNESS_KPI_OUTPUT_NAME);
        logger.debug("Checking received benchmark result {} ", correctnessKPI);
        int actual = benchmarkResult.getIntValueFor(ParrotTask.CHECKED_MESSAGES_OUTPUT_NAME);
        return actual == expectedMessagesCount;
    }
}