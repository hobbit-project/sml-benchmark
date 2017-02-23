package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.benchmark.Communication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
public class DummyRepeater extends SystemSideProtocol {
    private static final Logger logger = LoggerFactory.getLogger(DummyRepeater.class);

    public DummyRepeater(Communication.Builder communicationBuilder) {
        super(communicationBuilder);
    }

    @Override
    protected void handleDelivery(byte[] bytes) {
        try {
            logger.debug("Received message");
            getOutputCommunication().send(bytes);
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}