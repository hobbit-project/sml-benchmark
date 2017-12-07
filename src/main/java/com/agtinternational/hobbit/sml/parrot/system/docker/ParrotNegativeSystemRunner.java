package com.agtinternational.hobbit.sml.parrot.system.docker;

import com.agtinternational.hobbit.sml.BasicSystemComponent;
import com.agtinternational.hobbit.sml.parrot.system.ParrotNegativeSystemComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Roman Katerinenko
 */
public class ParrotNegativeSystemRunner {
    private static final Logger logger = LoggerFactory.getLogger(ParrotNegativeSystemRunner.class);

    public static final int CHECKED_MESSAGES_COUNT_BEFORE_FAIL = 0;

    public static void main(String... args) throws Exception {
        logger.debug("Staring negative system...");
        ParrotNegativeSystemRunner runner = new ParrotNegativeSystemRunner();
        runner.run();
    }

    public void run() throws Exception {
        logger.debug("Creating negative system component...");
        BasicSystemComponent systemComponent = new ParrotNegativeSystemComponent();
        logger.debug("Initializing negative system component...");
        systemComponent.init();
        logger.debug("Running negative system component...");
        systemComponent.run();
        logger.debug("Finished.");
        systemComponent.close();
        logger.debug("Closed.");
    }
}