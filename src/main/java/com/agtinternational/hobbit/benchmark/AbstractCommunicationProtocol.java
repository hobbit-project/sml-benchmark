package com.agtinternational.hobbit.benchmark;

/**
 * @author Roman Katerinenko
 */
public abstract class AbstractCommunicationProtocol {
    private final Communication.Builder communicationBuilder;

    private String errorMessage;
    private boolean isSuccessful;

    protected AbstractCommunicationProtocol(Communication.Builder communicationBuilder) {
        this.communicationBuilder = communicationBuilder;
    }

    public abstract boolean init();

    public abstract void execute();

    protected Communication.Builder getCommunicationBuilder() {
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