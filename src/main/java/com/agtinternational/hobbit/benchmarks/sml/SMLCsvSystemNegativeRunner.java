package com.agtinternational.hobbit.benchmarks.sml;


import com.agtinternational.hobbit.sdk.BasicSystemComponent;
import com.agtinternational.hobbit.sdk.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hobbit.core.Constants.SYSTEM_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystemNegativeRunner {
    private static final Logger logger = LoggerFactory.getLogger(SMLCsvSystemNegativeRunner.class);

    public static void main(String... args) throws Exception {
        logger.debug("Staring system...");
        SMLCsvSystemNegativeRunner dr = new SMLCsvSystemNegativeRunner();
        dr.run();
    }

    public void run() throws Exception {
        logger.debug("Creating system component...");
        String encodedModel = System.getenv().get(SYSTEM_PARAMETERS_MODEL_KEY);
        logger.debug("Params:{}", encodedModel);
        JenaKeyValue params = new JenaKeyValue.Builder().buildFrom(encodedModel);
        BasicSystemComponent sc = new SMLCsvSystemNegative(params);
        logger.debug("Initializing system component...");
        sc.init();
        logger.debug("Running system component...");
        sc.run();
        logger.debug("Finished.");
        sc.close();
        logger.debug("Closed.");
    }
}