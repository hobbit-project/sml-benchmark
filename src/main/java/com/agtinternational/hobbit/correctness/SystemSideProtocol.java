package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.Utils;
import com.agtinternational.hobbit.benchmark.AnomalyDetectionBenchmarkController;
import com.agtinternational.hobbit.benchmark.Communication;
import com.agtinternational.hobbit.benchmark.TerminationMessageProtocol;
import com.agtinternational.hobbit.io.NetworkCommunication;
import org.hobbit.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author Roman Katerinenko
 */
public abstract class SystemSideProtocol extends TerminationMessageProtocol {
    private static final Logger logger = LoggerFactory.getLogger(SystemSideProtocol.class);


    public SystemSideProtocol(NetworkCommunication.Builder communicationBuilder) {
        super(communicationBuilder, AnomalyDetectionBenchmarkController.TERMINATION_MESSAGE);
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
            setErrorMessage("Errors while executing the task");
            setSuccessful(false);
        }
        logger.debug("Finished.");
    }


    protected abstract void waitForFinishingProcessing() throws InterruptedException;

    @Override
    protected Communication.Consumer getInputConsumer() {
        return SystemSideProtocol.this::handleDelivery;
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
        return AnomalyDetectionBenchmarkController.CHARSET;
    }

    @Override
    protected int getInputPrefetchCount() {
        return 1;
    }

    @Override
    protected String getOutputCommunicationName() {
        return Utils.toPlatformQueueName(Constants.SYSTEM_2_EVAL_STORAGE_QUEUE_NAME);
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