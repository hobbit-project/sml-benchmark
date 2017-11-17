package com.agtinternational.hobbit.benchmarks.sml;

import com.agt.ferromatikdata.anomalydetector.ClusterRandomizer;
import com.agt.ferromatikdata.core.GeneratorTask;
import com.agt.ferromatikdata.core.GrowingGenerationTask;
import com.agt.ferromatikdata.core.MachineModel;
import com.agt.ferromatikdata.core.Machines;
import com.agtinternational.hobbit.sdk.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.agtinternational.hobbit.common.SMLConstants.BENCHMARK_MODE_DYNAMIC;
import static com.agtinternational.hobbit.common.SMLConstants.BENCHMARK_MODE_INPUT_NAME;
import static com.agtinternational.hobbit.common.SMLConstants.DATA_POINT_COUNT_INPUT_NAME;

/**
 * @author Roman Katerinenko
 */
public class BenchmarkModeInputHandler {
    private static final Pattern pattern = Pattern.compile(BENCHMARK_MODE_DYNAMIC + ":([0-9]+):([0-9]+)");
    private final KeyValue inputParams;
    private final Machines machines;

    public BenchmarkModeInputHandler(KeyValue inputParams, Machines machines) {
        this.inputParams = inputParams;
        this.machines = machines;
    }

    public GeneratorTask getGeneratorTask() {
        int dataPointCount = inputParams.getIntValueFor(DATA_POINT_COUNT_INPUT_NAME);
        String mode = inputParams.getStringValueFor(BENCHMARK_MODE_INPUT_NAME);
        if (mode != null && mode.startsWith(BENCHMARK_MODE_DYNAMIC)) {
            Matcher matcher = pattern.matcher(mode);
            if (!matcher.matches()) {
                throw new IllegalStateException("Wrong input format: " + mode);
            } else {
                int initialMachineCount = Integer.valueOf(matcher.group(1));
                int dataPointsBeforeNewMachine = Integer.valueOf(matcher.group(2));
                int machineModelsCount = machines.getMachineModels().size();
                if (initialMachineCount>machineModelsCount){
                    Machines machinesToAdd = Machines.newNMachinesDefault(ClusterRandomizer.newDefault(), initialMachineCount-machineModelsCount, 0);
                    for (MachineModel m : machinesToAdd.getMachineModels())
                        machines.addMachineModel(m);
                }
                return new GrowingGenerationTask(machines,
                        dataPointCount,
                        initialMachineCount,
                        dataPointsBeforeNewMachine);
            }
        } else {
            return GeneratorTask.newGeneratorTask(machines, dataPointCount);
        }
    }
}
