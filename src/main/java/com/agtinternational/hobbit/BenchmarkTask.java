package com.agtinternational.hobbit;

/**
 * @author Roman Katerinenko
 */
public interface BenchmarkTask extends Runnable {
    boolean isSuccessful();
    String getErrorMessage();
}