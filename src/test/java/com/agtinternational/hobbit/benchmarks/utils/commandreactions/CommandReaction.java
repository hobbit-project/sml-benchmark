package com.agtinternational.hobbit.benchmarks.utils.commandreactions;

import java.util.function.BiConsumer;

/**
 * @author Roman Katerinenko
 */
public interface CommandReaction extends BiConsumer<Byte, byte[]> {
}