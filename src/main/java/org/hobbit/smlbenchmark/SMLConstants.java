package org.hobbit.smlbenchmark;

/**
 * @author Roman Katerinenko
 */
public class SMLConstants {
    public static final String BENCHMARK_URI = "http://project-hobbit.eu/sml-benchmark-1.0/";
    public static final String PROBABILITY_THRESHOLD_INPUT_NAME = BENCHMARK_URI+"probabilityThreshold";
    public static final String DATA_POINT_COUNT_INPUT_NAME = BENCHMARK_URI+"dataPointCount";
    public static final String WINDOW_SIZE_INPUT_NAME = BENCHMARK_URI+"windowSize";
    public static final String TRANSITIONS_COUNT_INPUT_NAME = BENCHMARK_URI+"transitionsCount";
    public static final String MAX_CLUSTER_ITERATIONS_INPUT_NAME = BENCHMARK_URI+"maxClusterIterations";
    public static final String INTERVAL_NANOS_INPUT_NAME = BENCHMARK_URI+"interval";
    public static final String SEED_INPUT_NAME = BENCHMARK_URI+"seed";
    public static final String FORMAT_INPUT_NAME = BENCHMARK_URI+"format";
    public static final String MACHINE_COUNT_INPUT_NAME = BENCHMARK_URI+"machineCount";
    public static final String TIMEOUT_MINUTES_INPUT_NAME = BENCHMARK_URI+"timeoutMinutes";
    public static final String BENCHMARK_MODE_INPUT_NAME = BENCHMARK_URI+"benchmarkMode";
    //
    public static final String KPI_OUTPUT_NAME = BENCHMARK_URI+"Kpi";
    public static final String ANOMALY_MATCH_COUNT_OUTPUT_NAME = BENCHMARK_URI+"matchedAnomaliesCount";
    public static final String ANOMALY_MATCH_OUTPUT_NAME = BENCHMARK_URI+"anomalyMatch";
    public static final String AVERAGE_LATENCY_NANOS_OUTPUT_NAME = BENCHMARK_URI+"averageLatencyNanos";
    public static final String THROUGHPUT_BYTES_PER_SEC_OUTPUT_NAME = BENCHMARK_URI+"throughputBytesPerSecond";
    public static final String TERMINATION_TYPE_OUTPUT_NAME = BENCHMARK_URI+"terminationType";
    //
    public static final String TERMINATION_TYPE_NORMAL = "Terminated correctly.";
    public static final String ANOMALY_MATCH_SUCCESS = "Anomalies matched successfully";


    public static final int FORMAT_RDF = 0;
    public static final int FORMAT_CSV = 1;
    public static final int NO_TIMEOUT = -1;

    //
    private static final int BYTES_IN_ONE_MEASUREMENT_NTRIPLES = 164096;
    public static final int EXPECTED_DATA_POINTS_COUNT = 10 * 1024 * 1024 / BYTES_IN_ONE_MEASUREMENT_NTRIPLES; //63
    public static final int EXPECTED_ANOMALIES_COUNT = 3;

    private SMLConstants() {
    }
}