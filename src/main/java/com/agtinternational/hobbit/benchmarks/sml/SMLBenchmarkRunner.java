package com.agtinternational.hobbit.benchmarks.sml;

import com.agtinternational.hobbit.common.BenchmarkTask;
import com.agtinternational.hobbit.common.JenaKeyValue;
import com.agtinternational.hobbit.common.TaskBasedBenchmarkController;
import org.hobbit.core.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hobbit.core.Constants.BENCHMARK_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class SMLBenchmarkRunner {
    private static final Logger logger = LoggerFactory.getLogger(SMLBenchmarkRunner.class);

    public static void main(String... args) throws Exception {
        new SMLBenchmarkRunner().run();
    }

    private void run() throws Exception {
        logger.debug("Creating benchmark controller...");
        Component bc = createSMLBenchmarkController();
        logger.debug("Initializing common controller...");
        bc.init();
        logger.debug("Running benchmark controller...");
        bc.run();
        logger.debug("Finished.");
        bc.close();
        logger.debug("Closed.");
    }

    private Component createSMLBenchmarkController() {
        String encodedModel = System.getenv().get(BENCHMARK_PARAMETERS_MODEL_KEY);
        JenaKeyValue inputParameters = new JenaKeyValue.Builder().buildFrom(encodedModel);
        BenchmarkTask task = new SMLTask(inputParameters);
        int timeout = inputParameters.getIntValueFor(SMLConstants.TIMEOUT_MINUTES_INPUT_NAME);
        return new TaskBasedBenchmarkController(timeout, task);
    }
}