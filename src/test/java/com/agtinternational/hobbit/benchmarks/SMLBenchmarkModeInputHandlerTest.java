package com.agtinternational.hobbit.benchmarks;

import com.agt.ferromatikdata.core.GeneratorTask;
import com.agt.ferromatikdata.core.GeneratorTaskEntry;
import com.agt.ferromatikdata.core.GrowingGenerationTask;
import com.agt.ferromatikdata.core.Machines;
import com.agtinternational.hobbit.benchmarks.sml.BenchmarkModeInputHandler;
import com.agtinternational.hobbit.sdk.*;
import org.junit.Assert;
import org.junit.Test;

import static com.agtinternational.hobbit.common.SMLConstants.BENCHMARK_MODE_DYNAMIC;
import static com.agtinternational.hobbit.common.SMLConstants.BENCHMARK_MODE_INPUT_NAME;
import static com.agtinternational.hobbit.common.SMLConstants.DATA_POINT_COUNT_INPUT_NAME;
import static org.junit.Assert.assertEquals;

/**
 * @author Roman Katerinenko
 */
public class SMLBenchmarkModeInputHandlerTest {
    private int dataPointCountExpected;
    private int initialMachineCountExpected;
    private int dataPointsBeforeNewMachineExpected;
    private int machinesCount;

    @Test
    public void checkCorrectParsing() {
        KeyValue params = newInputParams();
        Machines machines = newMachines();
        GeneratorTask actual = new BenchmarkModeInputHandler(params, machines).getGeneratorTask();
        checkGenerationTask(actual);
    }

    private KeyValue newInputParams() {
        KeyValue keyValue = new KeyValue();
        dataPointCountExpected = 20;
        initialMachineCountExpected = 10;
        dataPointsBeforeNewMachineExpected = 11;
        String modeExpected = String.format(
                "%s:%d:%d",
                BENCHMARK_MODE_DYNAMIC,
                initialMachineCountExpected,
                dataPointsBeforeNewMachineExpected);
        keyValue.setValue(DATA_POINT_COUNT_INPUT_NAME, dataPointCountExpected);
        keyValue.setValue(BENCHMARK_MODE_INPUT_NAME, modeExpected);
        return keyValue;
    }

    private Machines newMachines() {
        machinesCount = 100;
        int firstMachineIndex = 0;
        return Machines.newNMachinesDefault(machinesCount, firstMachineIndex);
    }

    private void checkGenerationTask(GeneratorTask actual) {
        if (!(actual instanceof GrowingGenerationTask)) {
            Assert.fail("Wrong generator type class: " + actual.getClass());
        } else {
            GrowingGenerationTask task = (GrowingGenerationTask) actual;
            assertEquals(dataPointsBeforeNewMachineExpected, task.getDataPointsPerTask());
            int initialTaskCount = task.getInitialEntries().size();
            assertEquals(initialMachineCountExpected, initialTaskCount);
            int additionalTaskCounter = 0;
            while (task.getNextAdditionalEntry() != null) {
                additionalTaskCounter++;
            }
            assertEquals(machinesCount, additionalTaskCounter + initialTaskCount);
            GeneratorTaskEntry firstTask = task.getInitialEntries().get(0);
            assertEquals(dataPointCountExpected, firstTask.getDataPointCount());
        }
    }
}