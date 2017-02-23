package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.benchmark.AbstractCommunicationProtocol;
import com.agtinternational.hobbit.benchmark.Communication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Roman Katerinenko
 */
public abstract class BenchmarkSideProtocol extends AbstractCommunicationProtocol {
    private static final Logger logger = LoggerFactory.getLogger(BenchmarkSideProtocol.class);

    private final CountDownLatch inputCommunicationClosedBarrier = new CountDownLatch(1);

    private Communication outputCommunication;
    private Communication inputCommunication;
    private boolean outputCommunicationDeleted = false;
    private int waitingNanos;

    public BenchmarkSideProtocol(Communication.Builder communicationBuilder) {
        super(communicationBuilder);
    }

    public void setWaitingNanos(int waitingNanos) {
        this.waitingNanos = waitingNanos;
    }

    @Override
    public final void execute() {
        try {
            logger.debug("Initializing ...");
            initCommunication();
            logger.debug("Sending data...");
            sendData();
            logger.debug("Waiting until all sent data is received...");
            waitUntilAllSentDataReceived();
            logger.debug("Deleting communication...");
            deleteOutputCommunication();
            logger.debug("Waiting for input communication closed...");
            waitForInputCommunicationClosed();
        } catch (Exception e) {
            logger.error("Exception", e);
            setErrorMessage("Errors while executing the task");
            setSuccessful(false);
        } finally {
            deleteOutputCommunication();
        }
        logger.debug("Finished.");
    }

    protected void setOutputCommunication(Communication outputCommunication) {
        this.outputCommunication = outputCommunication;
    }

    protected void setInputCommunication(Communication inputCommunication) {
        this.inputCommunication = inputCommunication;
    }

    protected abstract void initCommunication() throws Exception;

    protected abstract void sendData() throws Exception;

    private void waitUntilAllSentDataReceived() throws InterruptedException, IOException {
        while (getOutputCommunication().getMessageCount() > 0) {
            Thread.sleep(waitingNanos);
        }
    }

    protected Communication getOutputCommunication() {
        return outputCommunication;
    }

    protected Communication getInputCommunication() {
        return inputCommunication;
    }

    protected CountDownLatch getInputCommunicationClosedBarrier() {
        return inputCommunicationClosedBarrier;
    }

    private void deleteOutputCommunication() {
        if (outputCommunicationDeleted) {
            return;
        }
        outputCommunicationDeleted = true;
        try {
            getOutputCommunication().delete();
        } catch (Exception e) {
            logger.debug("Exception while closing communication", e);
        }
    }

    private void waitForInputCommunicationClosed() throws InterruptedException {
        inputCommunicationClosedBarrier.await();
    }
}