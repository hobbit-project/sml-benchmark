package com.agtinternational.hobbit.sml.systems;


import com.agtinternational.hobbit.sdk.BasicSystemComponent;
import com.agtinternational.hobbit.sdk.io.AbstractCommunicationProtocol;
import com.agtinternational.hobbit.sdk.io.RabbitMqCommunication;
import com.agtinternational.hobbit.sml.benchmarks.parrot.DistortedRepeatProtocol;

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