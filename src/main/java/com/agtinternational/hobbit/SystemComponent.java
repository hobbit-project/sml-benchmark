package com.agtinternational.hobbit;

import org.hobbit.core.Constants;
import org.hobbit.core.components.AbstractSystemAdapter;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
public class SystemComponent extends AbstractSystemAdapter {
    private static final Logger logger = LoggerFactory.getLogger(SystemComponent.class);

    @Override
    public void init() throws Exception {
        super.init();
        String hobbitSessionId = getHobbitSessionId();
        if (hobbitSessionId.equals(Constants.HOBBIT_SESSION_ID_FOR_BROADCASTS) ||
                hobbitSessionId.equals(Constants.HOBBIT_SESSION_ID_FOR_PLATFORM_COMPONENTS)) {
            throw new IllegalStateException("Wrong hobbit session id. It must not be equal to HOBBIT_SESSION_ID_FOR_BROADCASTS or HOBBIT_SESSION_ID_FOR_PLATFORM_COMPONENTS");
        }
    }

    @Override
    public void receiveGeneratedData(byte[] bytes) {
        String receivedString = RabbitMQUtils.readString(bytes);
        logger.debug("System received data " + receivedString);
    }

    @Override
    public void receiveGeneratedTask(String s, byte[] bytes) {

    }
}