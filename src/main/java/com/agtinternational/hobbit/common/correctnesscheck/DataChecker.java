package com.agtinternational.hobbit.common.correctnesscheck;

/**
 * @author Roman Katerinenko
 */
public interface DataChecker<T> {
    void addActual(T actual);

    void addGold(T gold);

    boolean isCorrect();
}