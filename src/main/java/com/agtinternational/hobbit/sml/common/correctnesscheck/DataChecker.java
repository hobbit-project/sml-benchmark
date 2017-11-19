package com.agtinternational.hobbit.sml.common.correctnesscheck;

/**
 * @author Roman Katerinenko
 */
public interface DataChecker<T> {
    void addActual(T actual);

    void addGold(T gold);

    boolean isCorrect();
}