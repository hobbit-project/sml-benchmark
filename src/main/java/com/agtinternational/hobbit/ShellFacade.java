package com.agtinternational.hobbit;

import com.agtinternational.hobbit.benchmark.AnomalyDetectionBenchmarkController;

/**
 * @author Roman Katerinenko
 */
public class ShellFacade {
    public static void main(String...args) throws Exception {
        AnomalyDetectionBenchmarkController benchmarkController = new AnomalyDetectionBenchmarkController();
        benchmarkController.init();
        benchmarkController.run();
    }
}