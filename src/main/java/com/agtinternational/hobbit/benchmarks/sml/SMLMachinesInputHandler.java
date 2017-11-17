package com.agtinternational.hobbit.benchmarks.sml;

import com.agt.ferromatikdata.anomalydetector.ClusterRandomizer;
import com.agt.ferromatikdata.core.Machines;
import com.agtinternational.hobbit.sdk.*;

import static com.agtinternational.hobbit.common.SMLConstants.MACHINE_COUNT_INPUT_NAME;

/**
 * @author Roman Katerinenko
 */
class SMLMachinesInputHandler {
    private final int machineCount;

    SMLMachinesInputHandler(KeyValue inputParams) {
        machineCount = inputParams.getIntValueFor(MACHINE_COUNT_INPUT_NAME);
    }

    Machines getMachines() {
        return Machines.newNMachinesDefault(ClusterRandomizer.newDefault(), machineCount, 0);
    }
}