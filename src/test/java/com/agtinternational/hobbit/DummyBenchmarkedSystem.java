package com.agtinternational.hobbit;

import com.rabbitmq.client.MessageProperties;
import org.hobbit.core.Constants;
import org.hobbit.core.components.AbstractSystemAdapter;
import org.hobbit.core.data.RabbitQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Roman Katerinenko
 */
class DummyBenchmarkedSystem extends AbstractSystemAdapter {
    private static final Logger logger = LoggerFactory.getLogger(DummyBenchmarkedSystem.class);

    static final String OUT_QUEUE_NAME = "Dummy_system_out_queue";

    private RabbitQueue systemOutQueue;

    @Override
    public void init() throws Exception {
        super.init();
        String hobbitSessionId = getHobbitSessionId();
        if (hobbitSessionId.equals(Constants.HOBBIT_SESSION_ID_FOR_BROADCASTS) ||
                hobbitSessionId.equals(Constants.HOBBIT_SESSION_ID_FOR_PLATFORM_COMPONENTS)) {
            throw new IllegalStateException("Wrong hobbit session id. It must not be equal to HOBBIT_SESSION_ID_FOR_BROADCASTS or HOBBIT_SESSION_ID_FOR_PLATFORM_COMPONENTS");
        }
        String queueName = generateSessionQueueName(OUT_QUEUE_NAME);
        systemOutQueue = createDefaultRabbitQueue(queueName);
    }

    @Override
    public void receiveGeneratedData(byte[] bytes) {
        String exchangeName = "";
        String routingKey = dataGen2SystemQueue.name;
        try {
            systemOutQueue.getChannel().basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_BASIC, bytes);
        } catch (IOException e) {
            logger.error("Exception",e);
        }
    }

    @Override
    public void receiveGeneratedTask(String s, byte[] bytes) {
    }
}