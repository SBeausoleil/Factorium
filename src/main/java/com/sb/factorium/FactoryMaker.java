package com.sb.factorium;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;

public interface FactoryMaker<F extends Factory<String, ?>> {
    F factorise(Collection<Generator<?>> generators,
                Class<?> generatedType,
                Pair<String, Generator<?>> defaultGenerator);
}
