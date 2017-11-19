package com.agtinternational.hobbit.sml.benchmarks.parrot.io;

import com.agtinternational.hobbit.sdk.io.MinimalCommunication;

/**
 * @author Roman Katerinenko
 */
public final class InMemoryCommunication extends MinimalCommunication {

    private InMemoryCommunication(Builder builder) {
        super(builder);
    }

    @Override
    public void send(byte[] bytes) throws Exception {
        getConsumer().handleDelivery(bytes);
    }

    @Override
    public void send(String string) throws Exception {
        send(string.getBytes(getCharset()));
    }

    @Override
    public void close() throws Exception {
        // do nothing
    }

    public final static class Builder extends MinimalCommunication.Builder {
        @Override
        public MinimalCommunication build() throws Exception {
            return new InMemoryCommunication(this);
        }
    }
}