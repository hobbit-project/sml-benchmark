package com.agtinternational.hobbit.common;

/**
 * @author Roman Katerinenko
 */
public interface BenchmarkTask extends Runnable {
    boolean isSuccessful();

    KeyValue getResult();
}