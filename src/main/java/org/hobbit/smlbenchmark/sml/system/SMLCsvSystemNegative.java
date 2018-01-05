package org.hobbit.smlbenchmark.sml.system;


import org.hobbit.sdk.KeyValue;
import org.hobbit.sdk.io.RabbitMqCommunication;
import org.hobbit.smlbenchmark.common.system.BasicSystemComponent;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystemNegative extends BasicSystemComponent {
    public SMLCsvSystemNegative(KeyValue inputParameters) {
        super(new SMLSystemNegativeProtocol(new RabbitMqCommunication.Builder(), inputParameters));
    }
}