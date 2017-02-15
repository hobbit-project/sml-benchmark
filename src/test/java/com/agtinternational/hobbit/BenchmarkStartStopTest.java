package com.agtinternational.hobbit;

import com.agtinternational.hobbit.testutils.CommandQueueListener;
import com.agtinternational.hobbit.testutils.ComponentsExecutor;
import com.agtinternational.hobbit.testutils.ContainerSimulatedComponent;
import com.agtinternational.hobbit.testutils.commandreactions.StartBenchmarkWhenSystemAndBenchmarkReady;
import com.agtinternational.hobbit.testutils.commandreactions.StartSystemWhenItReady;
import com.agtinternational.hobbit.testutils.commandreactions.TerminateWhenBenchmarkControllerFinished;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.hobbit.core.Commands;
import org.hobbit.core.Constants;
import org.hobbit.core.components.Component;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.nio.ByteBuffer;

/**
 * Note requires running Rabbit MQ.
 * Something like 'sudo docker run -p 5672:5672 rabbitmq'
 *
 * @author Roman Katerinenko
 */
public class BenchmarkStartStopTest {
    @Rule
    public final EnvironmentVariables environmentVariables = new EnvironmentVariables();

    @Test
    public void checkBenchmarkStartStopCorrectly() throws Exception {
        String rabbitHostName = "127.0.0.1";
        String experimentId = "exp1";
        String systemUri = "http://agt.com/systems#sys122";
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
        AnomalyDetectionBenchmarkController benchmarkController = new AnomalyDetectionBenchmarkController();
        componentsExecutor.submit(benchmarkController);
        Component system = new DummyBenchmarkedSystem();
        ContainerSimulatedComponent containerComponent = new ContainerSimulatedComponent(system, systemContainerId);
        componentsExecutor.submit(containerComponent);
        commandQueueListener.blockCurrentThreadUntilTerminate();
        Assert.assertFalse(componentsExecutor.anyExceptions());
    }

    @Test
    public void checkBbenchmarkControllerAndSystemStartStopCorrectly() throws Exception {
        String rabbitHostName = "127.0.0.1";
        String experimentId = "exp1";
        String systemUri = "http://agt.com/systems#sys123332";
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
        AnomalyDetectionBenchmarkController benchmarkController = new AnomalyDetectionBenchmarkController();
        componentsExecutor.submit(benchmarkController);
        Component system = new SystemComponent();
        ContainerSimulatedComponent containerComponent = new ContainerSimulatedComponent(system, systemContainerId);
        componentsExecutor.submit(containerComponent);
        commandQueueListener.blockCurrentThreadUntilTerminate();
        Assert.assertFalse(componentsExecutor.anyExceptions());
    }

    @Test
    public void checkBenchmarkedSystemStartStopCorrectly() throws Exception {
        String rabbitHostName = "127.0.0.1";
        String experimentId = "exp144";
        setupCommunicationEnvironmentVariables(rabbitHostName, experimentId);
        ComponentsExecutor componentsExecutor = new ComponentsExecutor();
        CommandQueueListener commandQueueListener = new CommandQueueListener();
        String systemUri = "System1";
        commandQueueListener.setCommandReactions(new StartSystemWhenItReady(),
                (command, data) -> {
                    if (checkIfContainerTerminated(command, data, systemUri)) {
                        try {
                            commandQueueListener.terminate();
                            componentsExecutor.shutdown();
                        } catch (InterruptedException e) {
                            Assert.fail(e.getMessage());
                        }
                    }
                });
        componentsExecutor.submit(commandQueueListener);
        commandQueueListener.blockCurrentThreadUntilReady();
        setupSystemEnvironmentVariables();
        SystemComponent system = new SystemComponent();
        ContainerSimulatedComponent containerComponent = new ContainerSimulatedComponent(system, systemUri);
        componentsExecutor.submit(containerComponent);
        commandQueueListener.blockCurrentThreadUntilTerminate();
        Assert.assertFalse(componentsExecutor.anyExceptions());
    }

    private boolean checkIfContainerTerminated(byte command, byte[] data, String containerName) {
        if (command == Commands.DOCKER_CONTAINER_TERMINATED) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            String stringValue = RabbitMQUtils.readString(byteBuffer);
            return stringValue.equals(containerName);
        }
        return false;
    }

    private void setupBenchmarkEnvironmentVariables(String experimentId, String systemUri) {
        Model emptyModel = ModelFactory.createDefaultModel();
        environmentVariables.set(Constants.BENCHMARK_PARAMETERS_MODEL_KEY, RabbitMQUtils.writeModel2String(emptyModel));
        environmentVariables.set(Constants.HOBBIT_EXPERIMENT_URI_KEY, "http://w3id.org/hobbit/experiments#" + experimentId);
        environmentVariables.set(Constants.SYSTEM_URI_KEY, systemUri);
    }

    private void setupSystemEnvironmentVariables() {
        Model emptyModel = ModelFactory.createDefaultModel();
        environmentVariables.set(Constants.SYSTEM_PARAMETERS_MODEL_KEY, RabbitMQUtils.writeModel2String(emptyModel));
    }

    private void setupCommunicationEnvironmentVariables(String rabbitHostName, String experimentId) {
        environmentVariables.set(Constants.RABBIT_MQ_HOST_NAME_KEY, rabbitHostName);
        environmentVariables.set(Constants.HOBBIT_SESSION_ID_KEY, experimentId);
    }
}