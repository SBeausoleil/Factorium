package com.sb.factorium;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RecordingFactoryMaker implements FactoryMaker<RecordingFactory<String, ?>> {
    @Override
    public RecordingFactory<String, ?> factorise(Collection<Generator<?>> generators,
                                                 Class<?> target,
                                                 Pair<String, Generator<?>> defaultGenerator) {

        if (!MetaGeneratorUtil.allAssignableTo(generators, target)) {
            throw new IllegalArgumentException("Not all generators are assignable to " + target.getName());
        }

        Map<String, Generator<?>> coreMap = new HashMap<>();
        coreMap.put(defaultGenerator.getKey(), defaultGenerator.getValue());
        for (Generator<?> generator : generators) {
            if (!coreMap.containsValue(generator)) {
                String key = MetaGeneratorUtil.suggestName(generator);
                coreMap.put(key, generator);
            }
        }
        // Use raw types to coerce the usage of the wildcards
        return new RecordingFactory(target, defaultGenerator.getKey(), coreMap);
    }
}
