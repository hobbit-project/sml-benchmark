package org.hobbit.smlbenchmark.sml.system;

import org.hobbit.sdk.io.RabbitMqCommunication;
import org.hobbit.sdk.KeyValue;
import org.hobbit.smlbenchmark.common.system.BasicSystemComponent;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystem extends BasicSystemComponent {
    public SMLCsvSystem(KeyValue inputParameters) {
        super(new SMLSystemProtocol(new RabbitMqCommunication.Builder(), inputParameters));
    }
}