package com.sb.factorium;

import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class FactoryProvider {
    public static final String DEFAULT_KEY = "default";

    /**
     * Policy to use for the default key generation.
     */
    public enum DefaultKey {
        /**
         * Use the key "default" (as FactoryProvider.DEFAULT_KEY) for all default generators.
         */
        DEFAULT_KEY,
        /**
         * Use the name of the first generator found for a given type.
         */
        FIRST_GENERATOR
    }


    public static FactoryProvider make(Collection<Generator<?>> generators,
                                       DefaultKey keyPolicy,
                                       FactoryMaker<? extends Factory<String, ?>> factoryMaker) {
        // Organize generators by generated type
        Map<Class<?>, LinkedHashSet<Generator<?>>> types = new HashMap<>();
        for (Generator<?> generator : generators) {
            Class<?> provides = MetaGeneratorUtil.returnType(generator);
            LinkedHashSet<Generator<?>> setOfType = types.get(provides);
            if (setOfType == null) {
                setOfType = new LinkedHashSet<>();
                types.put(provides, setOfType);
            }
            setOfType.add(generator);
        }
        // Then create factories for each type
        for (Map.Entry<Class<?>, LinkedHashSet<Generator<?>>> generatorsOfType : types.entrySet()) {
            Generator<?> defaultGenerator = generatorsOfType.getValue().iterator().next();
            String defaultKey = computeDefaultKey(keyPolicy, defaultGenerator);
            factoryMaker.factorise(generatorsOfType.getValue(),
                    generatorsOfType.getKey(),
                    Pair.<String, Generator<?>>of(defaultKey, defaultGenerator));
        }
        return null;
    }

    private static String computeDefaultKey(DefaultKey keyPolicy, Generator<?> defaultGenerator) {
        String result = null;
        switch (keyPolicy) {
            case DEFAULT_KEY:
                result = DEFAULT_KEY;
                break;
            case FIRST_GENERATOR:
                result = MetaGeneratorUtil.suggestName(defaultGenerator);
                break;
            default:
                new IllegalArgumentException("Unrecognized keyPolicy! " + keyPolicy);
        }
        return result;
    }
}
