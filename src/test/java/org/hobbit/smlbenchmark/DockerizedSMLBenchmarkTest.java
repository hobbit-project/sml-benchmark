package org.hobbit.smlbenchmark;

import org.hobbit.core.components.Component;
import org.hobbit.sdk.docker.builders.BenchmarkDockerBuilder;
import org.hobbit.sdk.docker.builders.SystemAdapterDockerBuilder;
import org.hobbit.sdk.utils.commandreactions.MultipleCommandsReaction;
import org.hobbit.smlbenchmark.common.TaskBasedBenchmarkController;
import org.hobbit.smlbenchmark.common.benchmark.BenchmarkTask;
import org.hobbit.smlbenchmark.common.docker.SMLDockersBuilder;
import org.hobbit.smlbenchmark.sml.SMLTask;
import org.hobbit.smlbenchmark.sml.docker.AnalyticsBenchmarkDockerBuilder;
import org.hobbit.smlbenchmark.sml.docker.CommonSMLBenchmarksDockerBuilder;
import org.hobbit.smlbenchmark.sml.system.SMLCsvSystem;
import org.hobbit.smlbenchmark.sml.system.docker.SMLCsvNegativeSystemBuilder;
import org.hobbit.smlbenchmark.sml.system.docker.SMLCsvSystemDockerBuilder;
import org.hobbit.sdk.ComponentsExecutor;
import org.hobbit.sdk.docker.*;
import org.hobbit.smlbenchmark.sml.docker.SMLBenchmarkDockerBuilder;
import org.hobbit.sdk.utils.CommandQueueListener;
import org.hobbit.sdk.*;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.hobbit.sdk.CommonConstants.EXPERIMENT_URI;
import static org.hobbit.sdk.CommonConstants.SYSTEM_URI;
import static org.hobbit.smlbenchmark.SMLConstants.FORMAT_CSV;
import static org.hobbit.smlbenchmark.common.docker.SMLDockersBuilder.BENCHMARK_IMAGE_NAME;
import static org.hobbit.smlbenchmark.common.docker.SMLDockersBuilder.SYSTEM_IMAGE_NAME;

/**
 * @author Roman Katerinenko
 */
//todo check exactly one or less hobbit and hobbit-common networks exist
@RunWith(Parameterized.class)
public class DockerizedSMLBenchmarkTest extends EnvironmentVariablesWrapper {
    private static final String SESSION_ID = "http://example.com/sessionId1";
    private static final String FAKE_SYSTEM_URI = "http://example.com/fakeSystemId";
    private static final String RABBIT_HOST = "127.0.0.1";

    private AbstractDockerizer rabbitMqDockerizer;
    private ComponentsExecutor componentsExecutor;
    private CommandQueueListener commandQueueListener;

    //CommonSMLBenchmarksDockerBuilder benchmarkBuilder;
    BenchmarkDockerBuilder benchmarkBuilder;
    SystemAdapterDockerBuilder systemBuilder;
    //SMLDockersBuilder systemBuilder;

    Component benchmarkController;
    Component system;

    private enum SystemType {
        POSITIVE,
        NEGATIVE
    }

    private enum BenchmarkType {
        SML,
        ANALYTICS
    }

    //private AtomicBoolean correct = new AtomicBoolean(false);
    private int benchmarkOutputFormat;
    private boolean testShouldPass;
    private SystemType systemType;
    private BenchmarkType benchmarkType;

    @Parameterized.Parameters
    public static Collection parameters() throws Exception {
        return Arrays.asList(new Object[][]{
//                {FORMAT_CSV, false, SystemType.NEGATIVE, BenchmarkType.ANALYTICS},
//                {FORMAT_CSV, true, SystemType.POSITIVE, BenchmarkType.ANALYTICS},
//                {FORMAT_RDF, false, SystemType.POSITIVE, BenchmarkType.ANALYTICS},
                //{FORMAT_CSV, false, SystemType.NEGATIVE, BenchmarkType.SML},
                  {FORMAT_CSV, true, SystemType.POSITIVE, BenchmarkType.SML},
//                {FORMAT_RDF, false, SystemType.POSITIVE, BenchmarkType.SML}
        });
    }

    public DockerizedSMLBenchmarkTest(
            int benchmarkOutputFormat, boolean testShouldPass, SystemType systemType, BenchmarkType benchmarkType) {
        this.benchmarkOutputFormat = benchmarkOutputFormat;
        this.testShouldPass = testShouldPass;
        this.systemType = systemType;
        this.benchmarkType = benchmarkType;
    }

    public void init(Boolean useCachedImages) throws Exception {

        rabbitMqDockerizer = RabbitMqDockerizer.builder().build();

        setupCommunicationEnvironmentVariables(rabbitMqDockerizer.getHostName(), "session_"+String.valueOf(new Date().getTime()));
        setupBenchmarkEnvironmentVariables(EXPERIMENT_URI);
        setupGeneratorEnvironmentVariables(1,1);
        setupSystemEnvironmentVariables(SYSTEM_URI);

        benchmarkBuilder = newBenchmarkDockerizer(useCachedImages);
        systemBuilder = newSystemDockersBuilder(useCachedImages);

        benchmarkController = benchmarkBuilder.build();
        system = systemBuilder.build();
    }

    @Ignore
    @Test
    public void buildImages() throws Exception {

        init(false);
        ((AbstractDockerizer)benchmarkController).prepareImage();
        ((AbstractDockerizer)system).prepareImage();
    }

    @Test
    public void checkHealth() throws Exception {
        Boolean useCachedImages = true;

        init(useCachedImages);

        AbstractDockerizer rabbitMqDockerizer = RabbitMqDockerizer.builder().build();
        rabbitMqDockerizer.run();

        ComponentsExecutor componentsExecutor = new ComponentsExecutor();
        CommandQueueListener commandQueueListener = new CommandQueueListener();

        commandQueueListener.setCommandReactions(
                new MultipleCommandsReaction(componentsExecutor, commandQueueListener)
                        .systemContainerId(systemBuilder.getImageName())
                //new TerminateServicesWhenBenchmarkControllerFinished(componentsExecutor, commandQueueListener)
                );

        componentsExecutor.submit(commandQueueListener);
        commandQueueListener.waitForInitialisation();

//        KeyValue inputParams = createBenchmarkParameters();
//        BenchmarkTask task = new SMLTask(inputParams);
//        int timeout = inputParams.getIntValueFor(SMLConstants.TIMEOUT_MINUTES_INPUT_NAME);
//        benchmarkController = new TaskBasedBenchmarkController(timeout, task);

        componentsExecutor.submit(benchmarkController);

        //system = new SMLCsvSystem(newSystemParams());
        componentsExecutor.submit(system, systemBuilder.getImageName());

        commandQueueListener.waitForTermination();
        commandQueueListener.terminate();
        componentsExecutor.shutdown();
        rabbitMqDockerizer.stop();

        //Assert.assertTrue(correct.get());
        Assert.assertFalse(componentsExecutor.anyExceptions());
    }


    private BenchmarkDockerBuilder newBenchmarkDockerizer(Boolean useCachedImages) throws Exception {
        if (benchmarkType == BenchmarkType.SML) {
            return new BenchmarkDockerBuilder(new SMLBenchmarkDockerBuilder(BENCHMARK_IMAGE_NAME).useCachedImage(useCachedImages));
                    //.hobbitSessionId(SESSION_ID)
                    //.systemUri(FAKE_SYSTEM_URI)
                    //.skipLogsReading(true)
                    //.useCachedContainer()
                    //.build();
        } else if (benchmarkType == BenchmarkType.ANALYTICS) {
            return new BenchmarkDockerBuilder(new AnalyticsBenchmarkDockerBuilder().useCachedImage(useCachedImages));
                    //.hobbitSessionId(SESSION_ID)
                    //.systemUri(FAKE_SYSTEM_URI)
                    //.build();
        } else {
            return null;
        }
    }

    private SystemAdapterDockerBuilder newSystemDockersBuilder(Boolean useCachedImages) throws Exception {
        switch (systemType) {
            case POSITIVE:
                return new SystemAdapterDockerBuilder(new SMLCsvSystemDockerBuilder(SYSTEM_IMAGE_NAME).useCachedImage(useCachedImages));
                        //.hobbitSessionId(SESSION_ID)
                        //.systemUri(FAKE_SYSTEM_URI)
                        //.skipLogsReading()
                        //.build();
            case NEGATIVE:
            default:
                return new SystemAdapterDockerBuilder(new SMLCsvNegativeSystemBuilder().useCachedImage(useCachedImages));
                        //.hobbitSessionId(SESSION_ID)
                        //.systemUri(FAKE_SYSTEM_URI)
                        //.skipLogsReading()
                        //.build();
        }
    }

    private boolean checkReceivedModel(byte[] bytes) throws Exception {
        JenaKeyValue keyValue = new JenaKeyValue.Builder().buildFrom(bytes);
        String matchResult = keyValue.getStringValueFor(SMLConstants.ANOMALY_MATCH_OUTPUT_NAME);
        int matchedDataPoints = keyValue.getIntValueFor(SMLConstants.ANOMALY_MATCH_COUNT_OUTPUT_NAME);
        return (SMLConstants.ANOMALY_MATCH_SUCCESS.equals(matchResult)
                && SMLConstants.EXPECTED_ANOMALIES_COUNT == matchedDataPoints) == testShouldPass;
    }



//    public KeyValue createBenchmarkParameters() {
//
//        KeyValue kv = new KeyValue();
//        kv.setValue(SMLConstants.BENCHMARK_MODE_INPUT_NAME, SMLConstants.BENCHMARK_MODE_STATIC);
//        //kv.setValue(SMLConstants.BENCHMARK_MODE_INPUT_NAME, BENCHMARK_MODE_DYNAMIC+":10:1");
//        kv.setValue(SMLConstants.TIMEOUT_MINUTES_INPUT_NAME, SMLConstants.NO_TIMEOUT);
//        kv.setValue(SMLConstants.DATA_POINT_COUNT_INPUT_NAME, SMLConstants.EXPECTED_DATA_POINTS_COUNT);
//        kv.setValue(SMLConstants.MACHINE_COUNT_INPUT_NAME, SMLConstants.MACHINE_COUNT_DEFAULT);
//        kv.setValue(SMLConstants.PROBABILITY_THRESHOLD_INPUT_NAME, SMLConstants.PROBABILITY_THRESHOLD_DEFAULT);
//        kv.setValue(SMLConstants.WINDOW_SIZE_INPUT_NAME, SMLConstants.WINDOW_SIZE_DEFAULT);
//        kv.setValue(SMLConstants.TRANSITIONS_COUNT_INPUT_NAME, SMLConstants.TRANSITIONS_COUNT_DEFAULT);
//        kv.setValue(SMLConstants.MAX_CLUSTER_ITERATIONS_INPUT_NAME, SMLConstants.MAX_CLUSTER_ITERATIONS_DEFAULT);
//        kv.setValue(SMLConstants.INTERVAL_NANOS_INPUT_NAME, SMLConstants.INTERVAL_NANOS_DEFAULT);
//        kv.setValue(SMLConstants.SEED_INPUT_NAME, SMLConstants.SEED_DEFAULT);
//        kv.setValue(SMLConstants.FORMAT_INPUT_NAME, benchmarkOutputFormat);
//
//
//        return kv;
//    }


}
