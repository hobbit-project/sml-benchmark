package com.agtinternational.hobbit.io;

import com.agtinternational.hobbit.benchmark.Communication;

/**
 * @author Roman Katerinenko
 */
public class InMemoryCommunication extends Communication {

    public InMemoryCommunication(Builder builder) {
        super(builder);
    }

    @Override
    public void delete() throws Exception {
        getConsumer().onDelete();
    }

    @Override
    public void close() throws Exception {
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
    public long getMessageCount() {
        return 0;
    }
}