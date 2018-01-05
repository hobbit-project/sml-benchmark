package org.hobbit.smlbenchmark.parrot.system;


import org.hobbit.sdk.io.AbstractCommunicationProtocol;
import org.hobbit.sdk.io.RabbitMqCommunication;
import org.hobbit.smlbenchmark.common.system.BasicSystemComponent;

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