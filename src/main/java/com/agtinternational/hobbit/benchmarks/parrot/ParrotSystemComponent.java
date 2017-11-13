package com.agtinternational.hobbit.benchmarks.parrot;

import com.agtinternational.hobbit.common.AbstractCommunicationProtocol;
import com.agtinternational.hobbit.common.SystemComponent;
import com.agtinternational.hobbit.common.io.RabbitMqCommunication;

/**
 * @author Roman Katerinenko
 */
public class ParrotSystemComponent extends SystemComponent {
    public ParrotSystemComponent() {
        super(newSystemCommunicationProtocol());
    }

    private static AbstractCommunicationProtocol newSystemCommunicationProtocol() {
        RabbitMqCommunication.Builder communicationBuilder = new RabbitMqCommunication.Builder();
        return new RepeatProtocol(communicationBuilder);
    }
}