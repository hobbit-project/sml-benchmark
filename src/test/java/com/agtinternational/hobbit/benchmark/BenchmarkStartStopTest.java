package com.agtinternational.hobbit.benchmark;

import com.agtinternational.hobbit.testutils.CommandQueueListener;
import com.agtinternational.hobbit.testutils.ComponentsExecutor;
import com.agtinternational.hobbit.testutils.ContainerSimulatedComponent;
import com.agtinternational.hobbit.testutils.commandreactions.StartBenchmarkWhenSystemAndBenchmarkReady;
import com.agtinternational.hobbit.testutils.commandreactions.StartSystemWhenItReady;
import com.agtinternational.hobbit.testutils.commandreactions.TerminateWhenBenchmarkControllerFinished;
import org.hobbit.core.Commands;
import org.hobbit.core.components.Component;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Note requires running Rabbit MQ.
 * Something like 'sudo docker run -p 5672:5672 rabbitmq'
 *
 * @author Roman Katerinenko
 */
public class BenchmarkStartStopTest extends EnvironmentVariables {

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
        Component system = new SystemComponent(new DummyProtocol());
        ContainerSimulatedComponent containerComponent = new ContainerSimulatedComponent(system, systemContainerId);
        componentsExecutor.submit(containerComponent);
        commandQueueListener.blockCurrentThreadUntilTerminate();
        Assert.assertFalse(componentsExecutor.anyExceptions());
    }

    @Test
    public void checkBenchmarkControllerAndSystemStartStopCorrectly() throws Exception {
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
        Component system = new SystemComponent(new DummyProtocol());
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
        SystemComponent system = new SystemComponent(new DummyProtocol());
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

    private static class DummyProtocol extends AbstractCommunicationProtocol {
        public DummyProtocol() {
            super(null);
        }

        @Override
        public void execute() {
            // do nothing
        }
    }
}