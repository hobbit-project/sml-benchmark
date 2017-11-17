package com.agtinternational.hobbit.benchmarks.parrot;


import com.agtinternational.hobbit.sdk.BasicSystemComponent;
import com.agtinternational.hobbit.sdk.io.AbstractCommunicationProtocol;
import com.agtinternational.hobbit.sdk.io.RabbitMqCommunication;

/**
 * System component that fails intentionally
 *
 * @author Roman Katerinenko
 */
public class ParrotNegativeSystemComponent extends BasicSystemComponent {
    public ParrotNegativeSystemComponent() {
        super(newSystemCommunicationProtocol());
    }

    private static AbstractCommunicationProtocol newSystemCommunicationProtocol() {
        RabbitMqCommunication.Builder communicationBuilder = new RabbitMqCommunication.Builder();
        return new DistortedRepeatProtocol(communicationBuilder);
    }
}