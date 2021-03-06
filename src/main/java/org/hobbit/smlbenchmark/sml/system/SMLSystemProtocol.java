package org.hobbit.smlbenchmark.sml.system;

import com.agt.ferromatikdata.anomalydetector.AnomalyDetector;
import com.agt.ferromatikdata.anomalydetector.WithinMachineAnomaly;
import com.agt.ferromatikdata.formatting.RdfAnomalyFormatter;

import org.hobbit.sdk.io.NetworkCommunication;
import org.hobbit.sdk.*;
import org.hobbit.smlbenchmark.common.system.CorrectnessSystemProtocol;
import org.hobbit.smlbenchmark.sml.FerromatikAnomalyDetectorBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

/**
 * @author Roman Katerinenko
 */
class SMLSystemProtocol extends CorrectnessSystemProtocol {
    private static final Logger logger = LoggerFactory.getLogger(SMLSystemProtocol.class);

    private static final Charset CHARSET = Charset.forName("UTF-8");

    private AnomalyDetector anomalyDetector;

    SMLSystemProtocol(NetworkCommunication.Builder communicationBuilder, KeyValue kv) {
        super(communicationBuilder);
        try {
            anomalyDetector = new FerromatikAnomalyDetectorBuilder().buildFrom(kv, this::sendAnomaly);
        } catch (Exception e) {
//            todo fix exception from constructor
            anomalyDetector = null;
            logger.error("Exception", e);
        }
    }

    protected void sendAnomaly(WithinMachineAnomaly anomaly) {
        try {
            RdfAnomalyFormatter f = new RdfAnomalyFormatter(CHARSET);
            f.init();
            String string = f.format(anomaly);
            sendBytes(string.getBytes(CHARSET));
        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }

    @Override
    protected void waitForFinishingProcessing() throws InterruptedException {
        // do nothing
    }

    @Override
    protected void handleDelivery(byte[] bytes) {
        logger.debug("handleDelivery()");
        anomalyDetector.addNewDataPoint(new String(bytes, CHARSET));
    }
}