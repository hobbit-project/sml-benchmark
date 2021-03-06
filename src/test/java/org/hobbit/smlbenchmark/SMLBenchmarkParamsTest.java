package org.hobbit.smlbenchmark;

import org.hobbit.sdk.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.hobbit.core.rabbit.RabbitMQUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Roman Katerinenko
 */
public class SMLBenchmarkParamsTest {
    @Test
    public void checkInputFromPlatformController() throws Exception {
        byte[] bytesEncoding = newInputFromPlatformController();
        JenaKeyValue keyValue = new JenaKeyValue.Builder().buildFrom(bytesEncoding);
        Assert.assertTrue(SMLConstants.EXPECTED_DATA_POINTS_COUNT == keyValue.getIntValueFor(SMLConstants.DATA_POINT_COUNT_INPUT_NAME));
        double v = keyValue.getDoubleValueFor(SMLConstants.PROBABILITY_THRESHOLD_INPUT_NAME);
        Assert.assertTrue(Double.compare(0.005, v) == 0);
    }

    private byte[] newInputFromPlatformController() {
        Model model = ModelFactory.createDefaultModel();
        Resource subject = model.getResource("http://w3id.org/hobbit/experiments#New");
        model.add(subject, model.getProperty(SMLConstants.DATA_POINT_COUNT_INPUT_NAME), model.createTypedLiteral(SMLConstants.EXPECTED_DATA_POINTS_COUNT));
        model.add(subject, model.getProperty(SMLConstants.PROBABILITY_THRESHOLD_INPUT_NAME), model.createTypedLiteral(0.005));
        model.add(subject, model.getProperty("http://w3id.org/hobbit/vocab#involvesSystemInstance"), model.createTypedLiteral("http://example.com/system1"));
        model.add(subject, model.getProperty("http://w3id.org/hobbit/vocab#involvesBenchmark"), model.createTypedLiteral("http://example.com/benchmark1"));
        model.add(subject, model.getProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), model.getResource("http://w3id.org/hobbit/vocab#Experiment"));
        return RabbitMQUtils.writeModel(model);
    }
}