package com.agtinternational.hobbit.benchmarks.sml;

import com.agtinternational.hobbit.common.KeyValue;
import com.agtinternational.hobbit.common.SystemComponent;
import com.agtinternational.hobbit.common.io.RabbitMqCommunication;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystem extends SystemComponent {
    public SMLCsvSystem(KeyValue inputParameters) {
        super(new SMLSystemProtocol(new RabbitMqCommunication.Builder(), inputParameters));
    }
}