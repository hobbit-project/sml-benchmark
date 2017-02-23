package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.benchmark.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
class TestDataGenerator extends DataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);

    private int sendDataLength;
    private String lastSentMessage;

    private TestDataGenerator(TestDataGenerator.Builder builder) {
        super(builder);
    }

    @Override
    public void run() throws Exception {
        int count = 10;
        while (count-- > 0) {
            lastSentMessage = "Test str";
            logger.debug("Sending message to {}", getOutputCommunication().getName());
            getOutputCommunication().send(lastSentMessage);
            sendDataLength += lastSentMessage.length();
        }
    }

    int getSendDataLength() {
        return sendDataLength;
    }

    public static class Builder extends DataGenerator.Builder {
        public TestDataGenerator build() {
            return new TestDataGenerator(this);
        }
    }
}