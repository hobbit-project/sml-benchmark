package com.agtinternational.hobbit.io;

import com.agtinternational.hobbit.benchmark.Communication;

/**
 * @author Roman Katerinenko
 */
public class Repeater extends Communication {
    private final Communication outputCommunication1;
    private final Communication outputCommunication2;

    public Repeater(Communication outputCommunication1, Communication outputCommunication2) {
        super(new Communication.Builder());
        this.outputCommunication1 = outputCommunication1;
        this.outputCommunication2 = outputCommunication2;
    }

    @Override
    public String getName() {
        return "Repeater";
    }

    @Override
    public void delete() throws Exception {
        outputCommunication1.delete();
        outputCommunication2.delete();
    }

    @Override
    public void close() throws Exception {
        outputCommunication1.close();
        outputCommunication2.close();
    }

    @Override
    public void send(byte[] bytes) throws Exception {
        outputCommunication1.send(bytes);
        outputCommunication2.send(bytes);
    }

    @Override
    public void send(String string) throws Exception {
        send(string.getBytes(getCharset()));
    }

    @Override
    public long getMessageCount() {
        return outputCommunication1.getMessageCount() + outputCommunication2.getMessageCount();
    }
}