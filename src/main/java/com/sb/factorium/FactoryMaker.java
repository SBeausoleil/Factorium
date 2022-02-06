package com.sb.factorium;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.SortedSet;

public interface FactoryMaker<F extends Factory<String, ?>> {
    F factorise(SortedSet<Generator<?>> generators,
                Class<?> generatedType,
                Pair<String, Generator<?>> defaultGenerator);
}
