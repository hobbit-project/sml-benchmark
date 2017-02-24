package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.benchmark.AbstractCommunicationProtocol;
import com.agtinternational.hobbit.benchmark.AnomalyDetectionBenchmarkController;
import com.agtinternational.hobbit.benchmark.BenchmarkTaskImpl;
import com.agtinternational.hobbit.benchmark.DataChecker;
import com.agtinternational.hobbit.benchmark.EnvironmentVariables;
import com.agtinternational.hobbit.benchmark.SystemComponent;
import com.agtinternational.hobbit.io.RabbitMqCommunication;
import com.agtinternational.hobbit.testutils.CommandQueueListener;
import com.agtinternational.hobbit.testutils.ComponentsExecutor;
import com.agtinternational.hobbit.testutils.ContainerSimulatedComponent;
import com.agtinternational.hobbit.testutils.commandreactions.StartBenchmarkWhenSystemAndBenchmarkReady;
import com.agtinternational.hobbit.testutils.commandreactions.TerminateWhenBenchmarkControllerFinished;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Roman Katerinenko
 */
public class DataSendTest extends EnvironmentVariables {

    @Test
    public void systemTransmitSameData() throws Exception {
        String rabbitHostName = "127.0.0.1";
        String experimentId = "exp1";
        String systemUri = "http://agt.com/systems#sys10";
        setupCommunicationEnvironmentVariables(rabbitHostName, experimentId);
        setupBenchmarkEnvironmentVariables(experimentId, systemUri);
        setupSystemEnvironmentVariables();
        ComponentsExecutor componentsExecutor = new ComponentsExecutor();
        CommandQueueListener commandQueueListener = new CommandQueueListener();
        String systemContainerId = "1234kj34k";
        commandQueueListener.setCommandReactions(
                new TerminateWhenBenchmarkControllerFinished(commandQueueListener, componentsExecutor),
                new StartBenchmarkWhenSystemAndBenchmarkReady(systemContainerId));
        componentsExecutor.submit(commandQueueListener);
        commandQueueListener.blockCurrentThreadUntilReady();
        //
        AbstractCommunicationProtocol correctnessCheckProtocol = newBenchmarkSideProtocol();
        BenchmarkTaskImpl benchmarkTask = new BenchmarkTaskImpl(correctnessCheckProtocol);
        AnomalyDetectionBenchmarkController benchmarkController = new AnomalyDetectionBenchmarkController(benchmarkTask);
        componentsExecutor.submit(benchmarkController);
        //
        AbstractCommunicationProtocol systemSideProtocol = newSystemSideProtocol();
        SystemComponent systemComponent = new SystemComponent(systemSideProtocol);
        ContainerSimulatedComponent containerComponent = new ContainerSimulatedComponent(systemComponent, systemContainerId);
        componentsExecutor.submit(containerComponent);
        //
        commandQueueListener.blockCurrentThreadUntilTerminate();
        assertFalse(componentsExecutor.anyExceptions());
        assertTrue(benchmarkTask.isSuccessful());
    }


    private AbstractCommunicationProtocol newBenchmarkSideProtocol() {
        RabbitMqCommunication.Builder communicationBuilder = new RabbitMqCommunication.Builder();
        DataChecker dataChecker = new TestDataChecker();
        TestDataGenerator.Builder dataGeneratorBuilder = new TestDataGenerator.Builder();
        CorrectnessCheckProtocol correctnessCheckProtocol = new CorrectnessCheckProtocol(dataGeneratorBuilder, dataChecker, communicationBuilder);
        return correctnessCheckProtocol;
    }

    private AbstractCommunicationProtocol newSystemSideProtocol() {
        RabbitMqCommunication.Builder communicationBuilder = new RabbitMqCommunication.Builder();
        DummyRepeater dummyRepeater = new DummyRepeater(communicationBuilder);
        return dummyRepeater;
    }
}