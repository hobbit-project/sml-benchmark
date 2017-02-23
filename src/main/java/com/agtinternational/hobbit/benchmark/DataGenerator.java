package com.agtinternational.hobbit.benchmark;

/**
 * @author Roman Katerinenko
 */
public class DataGenerator {
    private final Communication outputCommunication;

    protected DataGenerator(Builder builder) {
        outputCommunication = builder.outputCommunication;
    }

    protected Communication getOutputCommunication() {
        return outputCommunication;
    }

    public void run() throws Exception {
        throw new IllegalStateException("Implement before calling.");
    }

    public static class Builder {
        private Communication outputCommunication;

        public final Builder outputCommunication(Communication outputCommunication) {
            this.outputCommunication = outputCommunication;
            return this;
        }

        public DataGenerator build() {
            return new DataGenerator(this);
        }
    }
}