package com.agtinternational.hobbit.sml.benchmarks.parrot;

import com.agtinternational.hobbit.sml.common.correctnesscheck.CorrectnessSystemProtocol;
import com.agtinternational.hobbit.sdk.io.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
public class DistortedRepeatProtocol extends CorrectnessSystemProtocol {
    private static final Logger logger = LoggerFactory.getLogger(DistortedRepeatProtocol.class);

    public DistortedRepeatProtocol(NetworkCommunication.Builder communicationBuilder) {
        super(communicationBuilder);
    }

    @Override
    protected void waitForFinishingProcessing() throws InterruptedException {
        // do nothing
    }

    @Override
    protected void handleDelivery(byte[] bytes) {
        try {
            String receiveMessage = new String(bytes, getCharset());
            logger.debug("Received message: {}", receiveMessage);
            String distortedMessage = receiveMessage + "_distorted";
            logger.debug("Sending distorted message: {}", distortedMessage);
            sendBytes(distortedMessage.getBytes(getCharset()));
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}