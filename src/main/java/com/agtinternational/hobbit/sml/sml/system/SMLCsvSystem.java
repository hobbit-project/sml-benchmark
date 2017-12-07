package com.agtinternational.hobbit.sml.sml.system;

import com.agtinternational.hobbit.sdk.io.RabbitMqCommunication;
import com.agtinternational.hobbit.sdk.KeyValue;
import com.agtinternational.hobbit.sml.BasicSystemComponent;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystem extends BasicSystemComponent {
    public SMLCsvSystem(KeyValue inputParameters) {
        super(new SMLSystemProtocol(new RabbitMqCommunication.Builder(), inputParameters));
    }
}