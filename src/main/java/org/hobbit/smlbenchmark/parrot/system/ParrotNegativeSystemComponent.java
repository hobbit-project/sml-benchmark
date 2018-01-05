package org.hobbit.smlbenchmark.parrot.system;


import org.hobbit.sdk.io.AbstractCommunicationProtocol;
import org.hobbit.sdk.io.RabbitMqCommunication;
import org.hobbit.smlbenchmark.parrot.DistortedRepeatProtocol;
import org.hobbit.smlbenchmark.common.system.BasicSystemComponent;

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