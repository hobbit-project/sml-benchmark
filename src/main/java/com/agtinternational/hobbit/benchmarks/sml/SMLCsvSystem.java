package com.agtinternational.hobbit.benchmarks.sml;


import com.agtinternational.hobbit.sdk.BasicSystemComponent;
import com.agtinternational.hobbit.sdk.io.RabbitMqCommunication;
import com.agtinternational.hobbit.sdk.KeyValue;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystem extends BasicSystemComponent {
    public SMLCsvSystem(KeyValue inputParameters) {
        super(new SMLSystemProtocol(new RabbitMqCommunication.Builder(), inputParameters));
    }
}