package com.agtinternational.hobbit.sml.sml.system;

import com.agtinternational.hobbit.sdk.*;
import com.agtinternational.hobbit.sml.BasicSystemComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hobbit.core.Constants.SYSTEM_PARAMETERS_MODEL_KEY;

/**
 * @author Roman Katerinenko
 */
public class SMLCsvSystemRunner {
    private static final Logger logger = LoggerFactory.getLogger(SMLCsvSystemRunner.class);

    public static void main(String... args) throws Exception {
        logger.debug("Staring system...");
        SMLCsvSystemRunner dr = new SMLCsvSystemRunner();
        dr.run();
    }

    public void run() throws Exception {
        logger.debug("Creating system component...");
        String encodedModel = System.getenv().get(SYSTEM_PARAMETERS_MODEL_KEY);
        logger.debug("Params:{}", encodedModel);
        JenaKeyValue params = new JenaKeyValue.Builder().buildFrom(encodedModel);
        BasicSystemComponent sc = new SMLCsvSystem(params);
        logger.debug("Initializing system component...");
        sc.init();
        logger.debug("Running system component...");
        sc.run();
        logger.debug("Finished.");
        sc.close();
        logger.debug("Closed.");
    }
}