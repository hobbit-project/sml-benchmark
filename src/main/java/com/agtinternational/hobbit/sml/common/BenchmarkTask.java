package com.agtinternational.hobbit.sml.common;

import com.agtinternational.hobbit.sdk.*;
/**
 * @author Roman Katerinenko
 */
public interface BenchmarkTask extends Runnable {
    boolean isSuccessful();

    KeyValue getResult();
}