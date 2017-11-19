package com.agtinternational.hobbit.sml.benchmarks.parrot;

import com.agtinternational.hobbit.sdk.io.Communication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
class StringDataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(StringDataGenerator.class);

    private final int messagesCount;
    private final Communication outputCommunication;

    StringDataGenerator(int messagesCount, Communication outputCommunication) {
        this.messagesCount = messagesCount;
        this.outputCommunication = outputCommunication;
    }

    public void run() throws Exception {
        int count = 1;
        while (count <= messagesCount) {
            logger.debug("Sending message to {}", outputCommunication.getName());
            outputCommunication.send("Test str " + count);
            count++;
        }
    }
}