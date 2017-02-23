package com.agtinternational.hobbit.benchmark;

/**
 * @author Roman Katerinenko
 */
public interface DataChecker extends Runnable {

    Communication.Consumer getInputConsumer();

    Communication.Consumer getGoldStandardConsumer();

    boolean isCorrect();
}