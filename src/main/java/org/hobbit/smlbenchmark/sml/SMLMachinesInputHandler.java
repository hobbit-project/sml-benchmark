package org.hobbit.smlbenchmark.sml;

import com.agt.ferromatikdata.anomalydetector.ClusterRandomizer;
import com.agt.ferromatikdata.core.Machines;
import org.hobbit.sdk.*;
import org.hobbit.smlbenchmark.SMLConstants;

/**
 * @author Roman Katerinenko
 */
class SMLMachinesInputHandler {
    private final int machineCount;

    SMLMachinesInputHandler(KeyValue inputParams) throws Exception {
        machineCount = inputParams.getIntValueFor(SMLConstants.MACHINE_COUNT_INPUT_NAME);
    }

    Machines getMachines() {
        return Machines.newNMachinesDefault(ClusterRandomizer.newDefault(), machineCount, 0);
    }
}