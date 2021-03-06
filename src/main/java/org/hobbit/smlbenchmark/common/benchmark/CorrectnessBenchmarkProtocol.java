package org.hobbit.smlbenchmark.common.benchmark;

import org.hobbit.sdk.io.Communication;
import org.hobbit.sdk.io.NetworkCommunication;
import org.hobbit.sdk.utils.CommandSender;
import org.hobbit.core.Commands;
import org.hobbit.core.Constants;
import org.hobbit.smlbenchmark.common.TaskBasedBenchmarkController;
import org.hobbit.smlbenchmark.Utils;
import org.hobbit.smlbenchmark.common.TerminationMessageProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author Roman Katerinenko
 */
public abstract class CorrectnessBenchmarkProtocol extends TerminationMessageProtocol {
    private static final Logger logger = LoggerFactory.getLogger(CorrectnessBenchmarkProtocol.class);

    public CorrectnessBenchmarkProtocol(NetworkCommunication.Builder communicationBuilder) {
        super(communicationBuilder, TaskBasedBenchmarkController.TERMINATION_MESSAGE);
    }

    protected void executeProtocol() throws Exception {
        logger.debug("Starting execution system...");
        startExecutionOnSystem();
        logger.debug("Initializing...");
        initProtocol();
        logger.debug("Starting data generator...");
        startDataGeneration();
        logger.debug("Sending termination message...");
        sendTerminationMessage();
        logger.debug("Waiting for termination message...");
        waitForTerminationMessage();
        logger.debug("Got termination message. Finished.");
    }

    protected abstract void startDataGeneration() throws Exception;

    protected abstract void initProtocol() throws Exception;

    private void startExecutionOnSystem() throws Exception {
        new CommandSender(Commands.TASK_GENERATION_FINISHED).send();
    }

    @Override
    protected String getInputHost() {
        return System.getenv().get(Constants.RABBIT_MQ_HOST_NAME_KEY);
    }

    @Override
    protected Charset getCharset() {
        return TaskBasedBenchmarkController.CHARSET;
    }

    @Override
    protected int getInputPrefetchCount() {
        return 1;
    }

    @Override
    protected String getInputCommunicationName() {
        return Utils.toPlatformQueueName(Constants.SYSTEM_2_EVAL_STORAGE_DEFAULT_QUEUE_NAME);
    }

    @Override
    protected String getOutputCommunicationName() {
        return Utils.toPlatformQueueName(Constants.DATA_GEN_2_SYSTEM_QUEUE_NAME);
    }

    @Override
    protected Communication.Consumer getOutputConsumer() {
        return null;
    }

    @Override
    protected String getOutputHost() {
        return getInputHost();
    }

    @Override
    protected int getOutputPrefetchCount() {
        return 1;
    }

}