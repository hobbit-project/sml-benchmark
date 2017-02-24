package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.io.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
public class DummyRepeater extends SystemSideProtocol {
    private static final Logger logger = LoggerFactory.getLogger(DummyRepeater.class);

    public DummyRepeater(NetworkCommunication.Builder communicationBuilder) {
        super(communicationBuilder);
    }

    @Override
    protected void waitForFinishingProcessing() throws InterruptedException {
        // do nothing
    }

    @Override
    protected void handleDelivery(byte[] bytes) {
        try {
            logger.debug("Repeating message: {}", new String(bytes, getCharset()));
            sendBytes(bytes);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}