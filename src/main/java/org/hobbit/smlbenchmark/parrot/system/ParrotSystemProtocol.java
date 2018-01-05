package org.hobbit.smlbenchmark.parrot.system;

import org.hobbit.smlbenchmark.common.system.CorrectnessSystemProtocol;
import org.hobbit.sdk.io.NetworkCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
class ParrotSystemProtocol extends CorrectnessSystemProtocol {
    private static final Logger logger = LoggerFactory.getLogger(ParrotSystemProtocol.class);

    ParrotSystemProtocol(NetworkCommunication.Builder communicationBuilder) {
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