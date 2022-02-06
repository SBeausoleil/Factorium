package com.sb.factorium;

import org.apache.commons.lang3.tuple.Pair;

import java.util.SortedSet;

/**
 * Transforms a set of generators into a factory
 *
 * @param <F> the type of factory created
 */
public interface FactoryMaker<F extends Factory<String, ?>> {
    /**
     * Transform generators into a factory
     *
     * @param generators       the generators to use
     * @param generatedType    the type of objects that the factory must create
     * @param defaultGenerator the default generator (key+generator) that the factory must use
     * @return a factory compliant with the arguments
     */
    F factorise(SortedSet<Generator<?>> generators,
                Class<?> generatedType,
                Pair<String, Generator<?>> defaultGenerator);
}
