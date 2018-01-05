package org.hobbit.smlbenchmark.common.benchmark;

/**
 * @author Roman Katerinenko
 */
public interface DataChecker<T> {
    void addActual(T actual);

    void addGold(T gold);

    boolean isCorrect();
}