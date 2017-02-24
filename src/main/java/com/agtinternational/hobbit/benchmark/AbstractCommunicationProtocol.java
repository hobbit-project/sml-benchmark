package com.agtinternational.hobbit.benchmark;

import com.agtinternational.hobbit.io.NetworkCommunication;

/**
 * @author Roman Katerinenko
 */
public abstract class AbstractCommunicationProtocol {
    private final NetworkCommunication.Builder communicationBuilder;

    private String errorMessage;
    private boolean isSuccessful;

    protected AbstractCommunicationProtocol(NetworkCommunication.Builder communicationBuilder) {
        this.communicationBuilder = communicationBuilder;
    }

    public abstract void execute();

    protected NetworkCommunication.Builder getCommunicationBuilder() {
        return communicationBuilder;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}