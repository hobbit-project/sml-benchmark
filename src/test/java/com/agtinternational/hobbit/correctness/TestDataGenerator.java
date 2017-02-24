package com.agtinternational.hobbit.correctness;

import com.agtinternational.hobbit.benchmark.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
class TestDataGenerator extends DataGenerator {
    private static final Logger logger = LoggerFactory.getLogger(TestDataGenerator.class);

    private String lastSentMessage;

    private TestDataGenerator(TestDataGenerator.Builder builder) {
        super(builder);
    }

    @Override
    public void run() throws Exception {
        int count = 1;
        while (count <= 10) {
            lastSentMessage = "Test str "+count;
            logger.debug("Sending message to {}", getOutputCommunication().getName());
            getOutputCommunication().send(lastSentMessage);
            count++;
        }
    }

    public static class Builder extends DataGenerator.Builder {
        public TestDataGenerator build() {
            return new TestDataGenerator(this);
        }
    }
}