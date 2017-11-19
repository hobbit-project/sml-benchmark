package com.agtinternational.hobbit.sml.systems;

import com.agtinternational.hobbit.sdk.BasicSystemComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
public class ParrotSystemRunner {
    private static final Logger logger = LoggerFactory.getLogger(ParrotSystemRunner.class);

    public static void main(String... args) throws Exception {
        logger.debug("Staring system...");
        ParrotSystemRunner parrotSystemRunner = new ParrotSystemRunner();
        parrotSystemRunner.run();
    }

    public void run() throws Exception {
        logger.debug("Creating system component...");
        BasicSystemComponent systemComponent = new ParrotSystemComponent();
        logger.debug("Initializing system component...");
        systemComponent.init();
        logger.debug("Running system component...");
        systemComponent.run();
        logger.debug("Finished.");
        systemComponent.close();
        logger.debug("Closed.");
    }
}