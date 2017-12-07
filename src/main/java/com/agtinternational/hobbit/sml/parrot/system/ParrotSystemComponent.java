package com.agtinternational.hobbit.sml.parrot.system;


import com.agtinternational.hobbit.sdk.io.AbstractCommunicationProtocol;
import com.agtinternational.hobbit.sdk.io.RabbitMqCommunication;
import com.agtinternational.hobbit.sml.BasicSystemComponent;

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