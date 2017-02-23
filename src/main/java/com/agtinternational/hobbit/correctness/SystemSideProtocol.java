package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.Utils;
import com.agtinternational.hobbit.benchmark.AbstractCommunicationProtocol;
import com.agtinternational.hobbit.benchmark.AnomalyDetectionBenchmarkController;
import com.agtinternational.hobbit.benchmark.Communication;
import org.hobbit.core.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @author Roman Katerinenko
 */
public abstract class SystemSideProtocol extends AbstractCommunicationProtocol {
    private static final Logger logger = LoggerFactory.getLogger(SystemSideProtocol.class);

    private final CountDownLatch inputCommunicationClosedBarrier = new CountDownLatch(1);

    private boolean outputCommunicationDeleted = false;
    private Communication inputCommunication;
    private Communication outputCommunication;
    private int waitingNanos;


    public SystemSideProtocol(Communication.Builder communicationBuilder) {
        super(communicationBuilder);
    }

    public void setWaitingNanos(int waitingNanos) {
        this.waitingNanos = waitingNanos;
    }

    protected Communication getInputCommunication() {
        return inputCommunication;
    }

    protected Communication getOutputCommunication() {
        return outputCommunication;
    }

    @Override
    public boolean init() {
        logger.debug("Opening communication...");
        try {
            String host = System.getenv().get(Constants.RABBIT_MQ_HOST_NAME_KEY);
            inputCommunication = getCommunicationBuilder()
                    .charset(AnomalyDetectionBenchmarkController.CHARSET)
                    .host(host)
                    .name(Utils.toPlatformQueueName(Constants.DATA_GEN_2_SYSTEM_QUEUE_NAME))
                    .prefetchCount(1)
                    .consumer(new Communication.Consumer() {
                        @Override
                        public void handleDelivery(byte[] bytes) {
                            SystemSideProtocol.this.handleDelivery(bytes);
                        }

                        @Override
                        public void onDelete() {
                            SystemSideProtocol.this.onDelete();
                        }
                    })
                    .build();
            logger.debug("Listening on queue {}", inputCommunication.getName());
            outputCommunication = getCommunicationBuilder()
                    .charset(AnomalyDetectionBenchmarkController.CHARSET)
                    .host(host)
                    .name(Utils.toPlatformQueueName(Constants.SYSTEM_2_EVAL_STORAGE_QUEUE_NAME))
                    .prefetchCount(1)
                    .build();
            logger.debug("Writing to queue {}", outputCommunication.getName());
            return true;
        } catch (Exception e) {
            logger.error("Exception", e);
            return false;
        }
    }

    protected abstract void handleDelivery(byte[] bytes);

    private void onDelete() {
        inputCommunicationClosedBarrier.countDown();
    }

    @Override
    public void execute() {
        try {
            logger.debug("Waiting for input communication closed...");
            waitForInputCommunicationClosed();
            logger.debug("Waiting until all sent data is received...");
            waitUntilAllSentDataReceived();
            logger.debug("Deleting communication");
            deleteOutputCommunication();
        } catch (Exception e) {
            logger.error("Exception", e);
        } finally {
            deleteOutputCommunication();
        }
    }

    private void waitForInputCommunicationClosed() throws InterruptedException {
        inputCommunicationClosedBarrier.await();
    }

    private void waitUntilAllSentDataReceived() throws IOException, InterruptedException {
        while (inputCommunication.getMessageCount() > 0) {
            Thread.sleep(waitingNanos);
        }
    }

    private void deleteOutputCommunication() {
        if (outputCommunicationDeleted) {
            return;
        }
        outputCommunicationDeleted = true;
        try {
            outputCommunication.delete();
        } catch (Exception e) {
            logger.debug("Exception while closing communication", e);
        }
    }
}