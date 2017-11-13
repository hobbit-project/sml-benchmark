package com.agtinternational.hobbit.benchmarks.sml;

import com.agt.ferromatikdata.anomalydetector.WithinMachineAnomaly;
import com.agtinternational.hobbit.common.KeyValue;
import com.agtinternational.hobbit.common.io.NetworkCommunication;

/**
 * @author Roman Katerinenko
 */
public class SMLSystemNegativeProtocol extends SMLSystemProtocol {
    private static final int SEND_WRONG_ANOMALY_AFTER = 2;

    private int anomalyCounter = 0;

    public SMLSystemNegativeProtocol(NetworkCommunication.Builder communicationBuilder, KeyValue inputParams) {
        super(communicationBuilder, inputParams);
    }

    @Override
    protected void sendAnomaly(WithinMachineAnomaly anomaly) {
        if (++anomalyCounter > SEND_WRONG_ANOMALY_AFTER) {
            double modifiedProbability = anomaly.getAnomaly().getProbability() + 1;
            anomaly.getAnomaly().setProbability(modifiedProbability);
        }
        super.sendAnomaly(anomaly);
    }
}
