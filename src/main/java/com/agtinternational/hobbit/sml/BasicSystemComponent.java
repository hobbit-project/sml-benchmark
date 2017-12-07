package com.agtinternational.hobbit.sml;

import com.agtinternational.hobbit.sdk.JenaKeyValue;
import com.agtinternational.hobbit.sdk.io.AbstractCommunicationProtocol;
import org.hobbit.core.Commands;
import org.hobbit.core.Constants;
import org.hobbit.core.components.AbstractCommandReceivingComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.hobbit.core.Constants.SYSTEM_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class BasicSystemComponent extends AbstractCommandReceivingComponent {
    private static final Logger logger = LoggerFactory.getLogger(BasicSystemComponent.class);

    private final AbstractCommunicationProtocol communicationProtocol;
    private final CountDownLatch startExecutionBarrier = new CountDownLatch(1);
    private Map params;

    public BasicSystemComponent(AbstractCommunicationProtocol communicationProtocol) {
        this.communicationProtocol = communicationProtocol;
    }

    @Override
    public void init() throws Exception {
        super.init();
        String hobbitSessionId = getHobbitSessionId();

        if (hobbitSessionId.equals(Constants.HOBBIT_SESSION_ID_FOR_BROADCASTS) ||
                hobbitSessionId.equals(Constants.HOBBIT_SESSION_ID_FOR_PLATFORM_COMPONENTS)) {
            throw new IllegalStateException("Wrong hobbit session id. It must not be equal to HOBBIT_SESSION_ID_FOR_BROADCASTS or HOBBIT_SESSION_ID_FOR_PLATFORM_COMPONENTS");
        }

        String encodedModel = System.getenv().get(SYSTEM_PARAMETERS_MODEL_KEY);
        logger.debug("Params:{}", encodedModel);
        params = new JenaKeyValue.Builder().buildFrom(encodedModel).toMap();
    }

    @Override
    public void run() throws Exception {
        logger.debug("Sending SYSTEM_READY_SIGNAL...");
        sendToCmdQueue(Commands.SYSTEM_READY_SIGNAL);
        logger.debug("Waiting for TASK_GENERATION_FINISHED...");
        startExecutionBarrier.await();
        logger.debug("Executing communication protocol...");
        communicationProtocol.execute();
        logger.debug("Finished");
    }

    @Override
    public void receiveCommand(byte command, byte[] data) {
        if (command == Commands.TASK_GENERATION_FINISHED) {
            startExecutionBarrier.countDown();
        }
    }
}