package com.agtinternational.hobbit.benchmarks.sml;

import com.agtinternational.hobbit.common.correctnesscheck.ComparablesMatcher;
import com.agtinternational.hobbit.common.correctnesscheck.*;
import com.agtinternational.hobbit.sdk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * @author Roman Katerinenko
 */
public class SMLDataChecker extends DataCheckerImpl<InstantAndText> {
    private static final Logger logger = LoggerFactory.getLogger(SMLDataChecker.class);

    private AnomalyDetector anomalyDetector;
    private Duration sumOfDeltas = Duration.ZERO;

    public SMLDataChecker(KeyValue inputParams, AnomalyDetectorBuilder anomalyDetectorBuilder) {
        super(new AnomalyMatcher());
        try {
            anomalyDetector = anomalyDetectorBuilder.buildFrom(inputParams, this::addGold);
        } catch (Exception e) {
            anomalyDetector = null;
            logger.error("Exception", e);
        }
    }

    public void addDataPoint(InstantAndText dataPoint) {
        logger.trace("Got benchmark data point: {}", dataPoint.getText());
        anomalyDetector.addNewDataPoint(dataPoint);
    }

    @Override
    protected void whenMatched(InstantAndText gold, InstantAndText actual) {
        Duration delta = Duration.between(gold.getInstant(), actual.getInstant());
        sumOfDeltas = sumOfDeltas.plus(delta);
    }

    public Duration getAverageLatency() {
        int checkedCount = getCheckedCount();
        return checkedCount > 0 ? sumOfDeltas.dividedBy(checkedCount) : null;
    }

    public interface AnomalyDetector {
        void addNewDataPoint(InstantAndText dataPoint);
    }

    public interface AnomalyListener {
        void onNewAnomaly(InstantAndText anomaly);
    }

    public interface AnomalyDetectorBuilder {
        AnomalyDetector buildFrom(KeyValue keyValue, AnomalyListener anomalyListener) throws Exception;
    }

    private static class AnomalyMatcher implements ComparablesMatcher<InstantAndText> {
        @Override
        public int compare(InstantAndText o1, InstantAndText o2) {
            String anomaly1 = o1.getText();
            String anomaly2 = o2.getText();
            try {
                boolean same = new com.agt.ferromatikdata.anomalydetector.AnomalyMatcher(anomaly1, anomaly2).isSame();
                return same ? 0 : anomaly1.compareTo(anomaly2);
            } catch (Exception e) {
                return -1;
            }
        }
    }
}