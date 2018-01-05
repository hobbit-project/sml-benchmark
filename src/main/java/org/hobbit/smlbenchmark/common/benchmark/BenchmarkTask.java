package org.hobbit.smlbenchmark.common.benchmark;

import org.hobbit.sdk.*;
/**
 * @author Roman Katerinenko
 */
public interface BenchmarkTask extends Runnable {
    boolean isSuccessful();

    KeyValue getResult();
}