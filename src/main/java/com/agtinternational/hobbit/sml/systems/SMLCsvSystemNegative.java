package com.agtinternational.hobbit.sml.systems;


import com.agtinternational.hobbit.sdk.BasicSystemComponent;
import com.agtinternational.hobbit.sdk.KeyValue;
import com.agtinternational.hobbit.sdk.io.RabbitMqCommunication;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystemNegative extends BasicSystemComponent {
    public SMLCsvSystemNegative(KeyValue inputParameters) {
        super(new SMLSystemNegativeProtocol(new RabbitMqCommunication.Builder(), inputParameters));
    }
}