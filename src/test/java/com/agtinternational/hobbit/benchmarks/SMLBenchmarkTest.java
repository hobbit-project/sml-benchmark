package com.agtinternational.hobbit.benchmarks;

import com.agtinternational.hobbit.common.*;
import com.agtinternational.hobbit.benchmarks.sml.SMLConstants;
import com.agtinternational.hobbit.benchmarks.sml.SMLCsvSystem;
import com.agtinternational.hobbit.benchmarks.sml.SMLCsvSystemNegative;
import com.agtinternational.hobbit.benchmarks.utils.CommandQueueListener;
import com.agtinternational.hobbit.benchmarks.utils.ComponentsExecutor;
import com.agtinternational.hobbit.benchmarks.utils.commandreactions.StartBenchmarkWhenSystemAndBenchmarkReady;
import com.agtinternational.hobbit.benchmarks.utils.commandreactions.TerminateServicesWhenBenchmarkControllerFinished;
import com.agtinternational.hobbit.benchmarks.sml.SMLTask;
import com.agtinternational.hobbit.benchmarks.utils.ContainerSimulatedComponent;
import com.agtinternational.hobbit.deployment.RabbitMqDockerizer;
import com.spotify.docker.client.exceptions.DockerCertificateException;
import com.spotify.docker.client.exceptions.DockerException;
import org.hobbit.core.Commands;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeoutException;

import static com.agtinternational.hobbit.benchmarks.sml.SMLConstants.*;
import static com.agtinternational.hobbit.deployment.CommonConstants.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Roman Katerinenko
 */
@RunWith(Parameterized.class)
public class SMLBenchmarkTest extends EnvironmentVariables {
    private static final String EXPERIMENT_URI = "http://agt.com/exp1";
    private static final String SYSTEM_URI = "http://agt.com/systems#sys10";
    private static final String SESSION_ID = EXPERIMENT_URI;
    private static final String RABBIT_HOST_NAME = "127.0.0.1";
    private static final String SYSTEM_CONTAINER_ID = "anythingGoesHere-weDontCheck";

    private enum SystemType {
        POSITIVE,
        NEGATIVE
    }

    private int benchmarkOutputFormat;
    private boolean testShouldPass;
    private SystemType systemType;

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {
        return Arrays.asList(new Object[][]{
                //{FORMAT_CSV, false, SystemType.NEGATIVE}, // 2 anomalies - fail
                //{FORMAT_CSV, true, SystemType.POSITIVE},  // 3 anomalies - success
                {FORMAT_RDF, false, SystemType.POSITIVE}  // 0 anomalies - fail
        });
    }

    public SMLBenchmarkTest(int benchmarkOutputFormat, boolean testShouldPass, SystemType systemType) {
        this.benchmarkOutputFormat = benchmarkOutputFormat;
        this.testShouldPass = testShouldPass;
        this.systemType = systemType;
    }

    @Test
    public void checkAnomaliesMatch() throws InterruptedException, TimeoutException, DockerCertificateException, DockerException {
        RabbitMqDockerizer rabbitMqDockerizer = RabbitMqDockerizer.builder()
                .host(RABBIT_HOST_NAME)
                .containerName(RABBIT_MQ_CONTAINER_NAME)
                .build();
        rabbitMqDockerizer.run();
        rabbitMqDockerizer.waitUntilRunning();
        setupCommunicationEnvironmentVariables(RABBIT_HOST_NAME, SESSION_ID);
        setupBenchmarkEnvironmentVariables(EXPERIMENT_URI);
        setupSystemEnvironmentVariables(SYSTEM_URI);
        ComponentsExecutor executor = new ComponentsExecutor();
        CommandQueueListener commandQueue = new CommandQueueListener();
        commandQueue.setCommandReactions(
                new TerminateServicesWhenBenchmarkControllerFinished(commandQueue, executor) {
                    @Override
                    public void accept(Byte command, byte[] data) {
                        if (command == Commands.BENCHMARK_FINISHED_SIGNAL) {
                            SMLBenchmarkTest.this.checkBenchmarkResult(data);
                        }
                        super.accept(command, data);
                    }
                },
                new StartBenchmarkWhenSystemAndBenchmarkReady(SYSTEM_CONTAINER_ID));
        executor.submit(commandQueue);
        commandQueue.waitForInitialisation();
        //
        KeyValue inputParams = createTaskParameters();
        BenchmarkTask task = new SMLTask(inputParams);
        int timeout = inputParams.getIntValueFor(SMLConstants.TIMEOUT_MINUTES_INPUT_NAME);
        executor.submit(new TaskBasedBenchmarkController(timeout, task));
        executor.submit(new ContainerSimulatedComponent(newSystem(), SYSTEM_CONTAINER_ID));
        //
        commandQueue.waitForTermination();
        assertFalse(executor.anyExceptions());
        assertTrue(task.isSuccessful() == testShouldPass);
        rabbitMqDockerizer.stopAndRemoveContainer();
    }

    private SystemComponent newSystem() {
        KeyValue systemParameters = createSystemParameters();
        if (systemType == SystemType.POSITIVE){
            return new SMLCsvSystem(systemParameters);
        } else {
            return new SMLCsvSystemNegative(systemParameters);
        }
    }

    private void checkBenchmarkResult(byte[] bytes) {
        JenaKeyValue keyValue = new JenaKeyValue.Builder().buildFrom(bytes);
        String matchResult = keyValue.getStringValueFor(SMLConstants.ANOMALY_MATCH_OUTPUT_NAME);
        assertTrue(SMLConstants.ANOMALY_MATCH_SUCCESS.equals(matchResult) == testShouldPass);
        int matchedDataPoints = keyValue.getIntValueFor(SMLConstants.ANOMALY_MATCH_COUNT_OUTPUT_NAME);
        assertTrue((SMLConstants.EXPECTED_ANOMALIES_COUNT == matchedDataPoints) == testShouldPass);
        double throughput = keyValue.getDoubleValueFor(SMLConstants.THROUGHPUT_BYTES_PER_SEC_OUTPUT_NAME);
        assertTrue(Double.compare(throughput, .0) >= 0);
        String actualTermination = keyValue.getStringValueFor(SMLConstants.TERMINATION_TYPE_OUTPUT_NAME);
        Assert.assertEquals(SMLConstants.TERMINATION_TYPE_NORMAL, actualTermination);
    }


    private KeyValue createTaskParameters() {

        KeyValue kv = new KeyValue();
        //kv.setValue(SMLConstants.BENCHMARK_MODE_INPUT_NAME, BENCHMARK_MODE_STATIC);
        kv.setValue(SMLConstants.BENCHMARK_MODE_INPUT_NAME, BENCHMARK_MODE_DYNAMIC+":10:1");
        kv.setValue(SMLConstants.TIMEOUT_MINUTES_INPUT_NAME, NO_TIMEOUT);
        kv.setValue(SMLConstants.DATA_POINT_COUNT_INPUT_NAME, EXPECTED_DATA_POINTS_COUNT);
        kv.setValue(SMLConstants.MACHINE_COUNT_INPUT_NAME, SMLConstants.MACHINE_COUNT_DEFAULT);
        kv.setValue(SMLConstants.PROBABILITY_THRESHOLD_INPUT_NAME, SMLConstants.PROBABILITY_THRESHOLD_DEFAULT);
        kv.setValue(SMLConstants.WINDOW_SIZE_INPUT_NAME, SMLConstants.WINDOW_SIZE_DEFAULT);
        kv.setValue(SMLConstants.TRANSITIONS_COUNT_INPUT_NAME, SMLConstants.TRANSITIONS_COUNT_DEFAULT);
        kv.setValue(SMLConstants.MAX_CLUSTER_ITERATIONS_INPUT_NAME, SMLConstants.MAX_CLUSTER_ITERATIONS_DEFAULT);
        kv.setValue(SMLConstants.INTERVAL_NANOS_INPUT_NAME, SMLConstants.INTERVAL_NANOS_DEFAULT);
        kv.setValue(SMLConstants.SEED_INPUT_NAME, SMLConstants.SEED_DEFAULT);
        kv.setValue(SMLConstants.FORMAT_INPUT_NAME, benchmarkOutputFormat);


        return kv;
    }

    private static KeyValue createSystemParameters() {
        KeyValue kv = new JenaKeyValue();
        kv.setValue(SMLConstants.MACHINE_COUNT_INPUT_NAME, SMLConstants.MACHINE_COUNT_DEFAULT);
        kv.setValue(SMLConstants.PROBABILITY_THRESHOLD_INPUT_NAME, SMLConstants.PROBABILITY_THRESHOLD_DEFAULT);
        kv.setValue(SMLConstants.WINDOW_SIZE_INPUT_NAME, SMLConstants.WINDOW_SIZE_DEFAULT);
        kv.setValue(SMLConstants.TRANSITIONS_COUNT_INPUT_NAME, SMLConstants.TRANSITIONS_COUNT_DEFAULT);
        kv.setValue(SMLConstants.MAX_CLUSTER_ITERATIONS_INPUT_NAME, SMLConstants.MAX_CLUSTER_ITERATIONS_DEFAULT);
        return kv;
    }
}