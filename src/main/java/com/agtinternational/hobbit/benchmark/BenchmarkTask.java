package com.agtinternational.hobbit.benchmark;

/**
 * @author Roman Katerinenko
 */
public interface BenchmarkTask extends Runnable {
    boolean isSuccessful();

    String getErrorMessage();
}