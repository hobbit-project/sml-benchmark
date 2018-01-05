package org.hobbit.smlbenchmark.common.system;

import org.hobbit.sdk.io.Communication;
import org.hobbit.sdk.io.NetworkCommunication;
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
public abstract class CorrectnessSystemProtocol extends TerminationMessageProtocol {
    private static final Logger logger = LoggerFactory.getLogger(CorrectnessSystemProtocol.class);


    public CorrectnessSystemProtocol(NetworkCommunication.Builder communicationBuilder) {
        super(communicationBuilder, TaskBasedBenchmarkController.TERMINATION_MESSAGE);
    }

    @Override
    public void executeProtocol() {
        try {
            logger.debug("Waiting for termination message...");
            waitForTerminationMessage();
            logger.debug("Waiting for finishing processing...");
            waitForFinishingProcessing();
            logger.debug("Sending termination message...");
            sendTerminationMessage();
        } catch (Exception e) {
            logger.error("Exception", e);
        }
        logger.debug("Finished.");
    }


    protected abstract void waitForFinishingProcessing() throws InterruptedException;

    @Override
    protected Communication.Consumer getInputConsumer() {
        return CorrectnessSystemProtocol.this::handleDelivery;
    }

    @Override
    protected String getInputHost() {
        return System.getenv().get(Constants.RABBIT_MQ_HOST_NAME_KEY);
    }

    @Override
    protected String getInputCommunicationName() {
        return Utils.toPlatformQueueName(Constants.DATA_GEN_2_SYSTEM_QUEUE_NAME);
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
    protected String getOutputCommunicationName() {
        return Utils.toPlatformQueueName(Constants.SYSTEM_2_EVAL_STORAGE_DEFAULT_QUEUE_NAME);
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

    protected abstract void handleDelivery(byte[] bytes);
}