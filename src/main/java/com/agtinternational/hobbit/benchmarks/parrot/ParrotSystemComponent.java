package com.agtinternational.hobbit.benchmarks.parrot;


import com.agtinternational.hobbit.sdk.BasicSystemComponent;
import com.agtinternational.hobbit.sdk.io.AbstractCommunicationProtocol;
import com.agtinternational.hobbit.sdk.io.RabbitMqCommunication;

/**
 * @author Roman Katerinenko
 */
public class ParrotSystemComponent extends BasicSystemComponent {
    public ParrotSystemComponent() {
        super(newSystemCommunicationProtocol());
    }

    private static AbstractCommunicationProtocol newSystemCommunicationProtocol() {
        RabbitMqCommunication.Builder communicationBuilder = new RabbitMqCommunication.Builder();
        return new ParrotSystemProtocol(communicationBuilder);
    }
}