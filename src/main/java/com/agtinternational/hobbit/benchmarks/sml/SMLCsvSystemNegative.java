package com.agtinternational.hobbit.benchmarks.sml;

import com.agtinternational.hobbit.common.KeyValue;
import com.agtinternational.hobbit.common.SystemComponent;
import com.agtinternational.hobbit.common.io.RabbitMqCommunication;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystemNegative extends SystemComponent {
    public SMLCsvSystemNegative(KeyValue inputParameters) {
        super(new SMLSystemNegativeProtocol(new RabbitMqCommunication.Builder(), inputParameters));
    }
}