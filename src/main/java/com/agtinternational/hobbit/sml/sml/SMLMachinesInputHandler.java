package com.agtinternational.hobbit.sml.sml;

import com.agt.ferromatikdata.anomalydetector.ClusterRandomizer;
import com.agt.ferromatikdata.core.Machines;
import com.agtinternational.hobbit.sdk.*;
import com.agtinternational.hobbit.sml.SMLConstants;

/**
 * @author Roman Katerinenko
 */
class SMLMachinesInputHandler {
    private final int machineCount;

    SMLMachinesInputHandler(KeyValue inputParams) {
        machineCount = inputParams.getIntValueFor(SMLConstants.MACHINE_COUNT_INPUT_NAME);
    }

    Machines getMachines() {
        return Machines.newNMachinesDefault(ClusterRandomizer.newDefault(), machineCount, 0);
    }
}