package com.agtinternational.hobbit.benchmark;

import com.agtinternational.hobbit.io.CommandSender;
import org.hobbit.core.Commands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Task that starts benchmarked system and executes testing protocol.
 *
 * @author Roman Katerinenko
 */
public class BenchmarkTaskImpl implements BenchmarkTask {
    private static final Logger logger = LoggerFactory.getLogger(BenchmarkTaskImpl.class);

    private final AbstractCommunicationProtocol communicationProtocol;

    private String errorMessage;
    private boolean isSuccessful = true;

    public BenchmarkTaskImpl(AbstractCommunicationProtocol communicationProtocol) {
        this.communicationProtocol = communicationProtocol;
    }

    private void startSystem() throws Exception {
        new CommandSender(Commands.TASK_GENERATION_FINISHED).send();
    }

    @Override
    public void run() {
        try {
            logger.debug("Starting system...");
            startSystem();
            logger.debug("Executing task...");
            communicationProtocol.execute();
            setErrorMessage(communicationProtocol.getErrorMessage());
            setSuccessful(communicationProtocol.isSuccessful());
        } catch (Exception e) {
            isSuccessful = false;
            errorMessage = "Unable to send data to system";
            logger.error("Exception ", e);
        }
        logger.debug("Finished.");
    }

    @Override
    public boolean isSuccessful() {
        return isSuccessful;
    }

    @Override
    public String getErrorMessage() {
        return errorMessage;
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }
}