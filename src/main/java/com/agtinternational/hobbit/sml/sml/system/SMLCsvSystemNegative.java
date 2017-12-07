package com.agtinternational.hobbit.sml.sml.system;


import com.agtinternational.hobbit.sdk.KeyValue;
import com.agtinternational.hobbit.sdk.io.RabbitMqCommunication;
import com.agtinternational.hobbit.sml.BasicSystemComponent;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystemNegative extends BasicSystemComponent {
    public SMLCsvSystemNegative(KeyValue inputParameters) {
        super(new SMLSystemNegativeProtocol(new RabbitMqCommunication.Builder(), inputParameters));
    }
}