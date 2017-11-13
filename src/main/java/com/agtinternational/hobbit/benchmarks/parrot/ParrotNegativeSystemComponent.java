package com.agtinternational.hobbit.benchmarks.parrot;

import com.agtinternational.hobbit.common.AbstractCommunicationProtocol;
import com.agtinternational.hobbit.common.SystemComponent;
import com.agtinternational.hobbit.common.io.RabbitMqCommunication;

/**
 * System component that fails intentionally
 *
 * @author Roman Katerinenko
 */
public class ParrotNegativeSystemComponent extends SystemComponent {
    public ParrotNegativeSystemComponent() {
        super(newSystemCommunicationProtocol());
    }

    private static AbstractCommunicationProtocol newSystemCommunicationProtocol() {
        RabbitMqCommunication.Builder communicationBuilder = new RabbitMqCommunication.Builder();
        return new DistortedRepeatProtocol(communicationBuilder);
    }
}