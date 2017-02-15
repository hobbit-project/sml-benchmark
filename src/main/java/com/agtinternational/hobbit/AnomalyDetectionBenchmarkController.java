package com.agtinternational.hobbit;

import org.hobbit.core.Commands;
import org.hobbit.core.Constants;
import org.hobbit.core.components.AbstractBenchmarkController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * @author Roman Katerinenko
 */
public class AnomalyDetectionBenchmarkController extends AbstractBenchmarkController {
    private static final Logger logger = LoggerFactory.getLogger(AnomalyDetectionBenchmarkController.class);

    private final Collection<BenchmarkTask> tasks;

    public AnomalyDetectionBenchmarkController(BenchmarkTask... tasks) {
        this.tasks = new ArrayList<>(Arrays.asList(tasks));
    }

    @Override
    public void init() throws Exception {
        super.init();
        Map<String, String> environment = System.getenv();
        if (!environment.containsKey(Constants.SYSTEM_URI_KEY)) {
            throw new IllegalStateException("System URI must not be null");
        }
        String hobbitSessionId = getHobbitSessionId();
        if (hobbitSessionId.equals(Constants.HOBBIT_SESSION_ID_FOR_BROADCASTS) ||
                hobbitSessionId.equals(Constants.HOBBIT_SESSION_ID_FOR_PLATFORM_COMPONENTS)) {
            throw new IllegalStateException("Wrong hobbit session id. It must not be equal to HOBBIT_SESSION_ID_FOR_BROADCASTS or HOBBIT_SESSION_ID_FOR_PLATFORM_COMPONENTS");
        }
    }

    @Override
    protected void executeBenchmark() throws Exception {
        logger.debug("Start executing...");
        for (BenchmarkTask task : tasks) {
            try {
                task.run();
                if (!task.isSuccessful()) {
                    reportTaskError(task);
                    break;
                }
            } catch (Exception e) {
                logger.error("Task {} threw an exception ", task.toString());
                logger.error("", e);
            }
        }
        sendToCmdQueue(Commands.BENCHMARK_FINISHED_SIGNAL);
    }

    private void reportTaskError(BenchmarkTask task) {

    }

    private void startSystem() throws IOException {
        sendToCmdQueue(Commands.TASK_GENERATION_FINISHED);
    }
}