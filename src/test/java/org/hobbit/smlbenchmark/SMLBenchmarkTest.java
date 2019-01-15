package org.hobbit.smlbenchmark;

import org.hobbit.core.components.Component;

import org.hobbit.sdk.utils.commandreactions.MultipleCommandsReaction;
import org.hobbit.smlbenchmark.common.TaskBasedBenchmarkController;
import org.hobbit.smlbenchmark.common.benchmark.BenchmarkTask;
import org.hobbit.smlbenchmark.common.system.BasicSystemComponent;
import org.hobbit.smlbenchmark.sml.system.SMLCsvSystem;
import org.hobbit.smlbenchmark.sml.system.SMLCsvSystemNegative;
import org.hobbit.smlbenchmark.sml.SMLTask;
import org.hobbit.sdk.ComponentsExecutor;
import org.hobbit.sdk.docker.RabbitMqDockerizer;
import org.hobbit.sdk.utils.CommandQueueListener;

import org.hobbit.sdk.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.hobbit.smlbenchmark.SMLConstants.FORMAT_CSV;
import static org.hobbit.smlbenchmark.common.docker.SMLDockersBuilder.SYSTEM_IMAGE_NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Roman Katerinenko
 */
@RunWith(Parameterized.class)
public class SMLBenchmarkTest extends EnvironmentVariablesWrapper {
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
                {FORMAT_CSV, true, SystemType.POSITIVE},  // 3 anomalies - success
                //{SMLConstants.FORMAT_RDF, false, SystemType.POSITIVE}  // 0 anomalies - fail
        });
    }

    public SMLBenchmarkTest(int benchmarkOutputFormat, boolean testShouldPass, SystemType systemType) {
        this.benchmarkOutputFormat = benchmarkOutputFormat;
        this.testShouldPass = testShouldPass;
        this.systemType = systemType;
    }

    @Test
    public void checkHealth() throws Exception {
        RabbitMqDockerizer rabbitMqDockerizer = RabbitMqDockerizer.builder()
                .build();
        rabbitMqDockerizer.run();

        setupCommunicationEnvironmentVariables(rabbitMqDockerizer.getHostName(), "session_"+String.valueOf(new Date().getTime()));
        setupBenchmarkEnvironmentVariables(EXPERIMENT_URI);
        setupSystemEnvironmentVariables(SYSTEM_URI);


        ComponentsExecutor componentsExecutor = new ComponentsExecutor();
        CommandQueueListener commandQueueListener = new CommandQueueListener();

        commandQueueListener.setCommandReactions(
                new MultipleCommandsReaction(componentsExecutor, commandQueueListener)
                        .systemContainerId(SYSTEM_IMAGE_NAME)
        );


        componentsExecutor.submit(commandQueueListener);
        commandQueueListener.waitForInitialisation();

        KeyValue benchmarkParams = createBenchmarkParameters(benchmarkOutputFormat);

        int timeout = benchmarkParams.getIntValueFor(SMLConstants.TIMEOUT_MINUTES_INPUT_NAME);
        BenchmarkTask task = new SMLTask(benchmarkParams);

        Component benchmark = new TaskBasedBenchmarkController(timeout, task);
        Component system = newSystem();

        componentsExecutor.submit(benchmark);
        componentsExecutor.submit(system, SYSTEM_IMAGE_NAME);
        //
        commandQueueListener.waitForTermination();
        assertFalse(componentsExecutor.anyExceptions());
        assertTrue(task.isSuccessful() == testShouldPass);
        rabbitMqDockerizer.stop();
    }

    public BasicSystemComponent newSystem() {
        KeyValue systemParameters = createSystemParameters();
        if (systemType == SystemType.POSITIVE){
            return new SMLCsvSystem(systemParameters);
        } else {
            return new SMLCsvSystemNegative(systemParameters);
        }
    }

    private void checkBenchmarkResult(byte[] bytes) throws Exception {
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


    public static JenaKeyValue createBenchmarkParameters(int benchmarkOutputFormat) {


        JenaKeyValue kv = new JenaKeyValue();
        kv.setValue(SMLConstants.BENCHMARK_MODE_INPUT_NAME, "static");
        //kv.setValue(SMLConstants.BENCHMARK_MODE_INPUT_NAME, BENCHMARK_MODE_DYNAMIC+":10:1");
        kv.setValue(SMLConstants.TIMEOUT_MINUTES_INPUT_NAME, -1);
        kv.setValue(SMLConstants.DATA_POINT_COUNT_INPUT_NAME, SMLConstants.EXPECTED_DATA_POINTS_COUNT);
        kv.setValue(SMLConstants.MACHINE_COUNT_INPUT_NAME, 1);
        kv.setValue(SMLConstants.PROBABILITY_THRESHOLD_INPUT_NAME, 0.005);
        kv.setValue(SMLConstants.WINDOW_SIZE_INPUT_NAME, 50);
        kv.setValue(SMLConstants.TRANSITIONS_COUNT_INPUT_NAME, 5);
        kv.setValue(SMLConstants.MAX_CLUSTER_ITERATIONS_INPUT_NAME, 50);
        kv.setValue(SMLConstants.INTERVAL_NANOS_INPUT_NAME, 10);
        kv.setValue(SMLConstants.SEED_INPUT_NAME, 123);
        kv.setValue(SMLConstants.FORMAT_INPUT_NAME, benchmarkOutputFormat);


        return kv;
    }

    private static JenaKeyValue createSystemParameters(){
        JenaKeyValue kv = new JenaKeyValue();
        kv.setValue(SMLConstants.MACHINE_COUNT_INPUT_NAME, 1);
        kv.setValue(SMLConstants.PROBABILITY_THRESHOLD_INPUT_NAME, 0.005);
        kv.setValue(SMLConstants.WINDOW_SIZE_INPUT_NAME, 50);
        kv.setValue(SMLConstants.TRANSITIONS_COUNT_INPUT_NAME, 5);
        kv.setValue(SMLConstants.MAX_CLUSTER_ITERATIONS_INPUT_NAME, 50);
        return kv;
    }
}