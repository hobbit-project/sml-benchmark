package org.hobbit.smlbenchmark.sml;

import com.agt.ferromatikdata.anomalydetector.AnomalyDetector;
import com.agt.ferromatikdata.anomalydetector.GeneratedDatasetConstants;
import com.agt.ferromatikdata.anomalydetector.Metadata;
import com.agt.ferromatikdata.anomalydetector.MetadataProducer;
import com.agt.ferromatikdata.clustering.DebsClusteringFactory;
import com.agt.ferromatikdata.core.Machines;
import org.hobbit.sdk.*;
import org.hobbit.smlbenchmark.SMLConstants;

import java.io.IOException;

/**
 * @author Roman Katerinenko
 */
public class FerromatikAnomalyDetectorBuilder {
    public AnomalyDetector buildFrom(
            KeyValue inputParams,
            com.agt.ferromatikdata.anomalydetector.AnomalyDetector.AnomalyListener anomalyListener)
            throws Exception {
        return new com.agt.ferromatikdata.anomalydetector.AnomalyDetectorImpl.Builder()
                .metadata(createMetadata(inputParams))
                .anomalyListener(anomalyListener)
                .transitionsAmount(inputParams.getIntValueFor(SMLConstants.TRANSITIONS_COUNT_INPUT_NAME))
                .maxClusteringIterations(inputParams.getIntValueFor(SMLConstants.MAX_CLUSTER_ITERATIONS_INPUT_NAME))
                .clusteringPrecision(0.00001)
                .clusteringFactory(new DebsClusteringFactory())
                .windowSize(inputParams.getIntValueFor(SMLConstants.WINDOW_SIZE_INPUT_NAME))
                .nonDoubleDimensionIds(GeneratedDatasetConstants.MACHINE_DATE_TIME_TEXT_DIMENSIONS.getAllIds())
                .build();
    }

    private static Metadata createMetadata(KeyValue inputParams) throws Exception {
        double probabilityThreshold = inputParams.getDoubleValueFor(SMLConstants.PROBABILITY_THRESHOLD_INPUT_NAME);
        Machines machines = new SMLMachinesInputHandler(inputParams).getMachines();
        MetadataProducer metadataProvider = new MetadataProducer(machines, probabilityThreshold);
        metadataProvider.run();
        return metadataProvider.getMetadata();
    }
}