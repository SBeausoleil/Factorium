package com.sb.factorium;

import com.sb.factorium.reflection.DefaultSurrogate;
import com.sb.factorium.reflection.ReflectionUtil;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Regroup factories for an application.
 * Create one using the FactoryProvider.make() method and giving it the generators to use.
 * <p>
 * Usage example:
 * <pre>{@code
 * FactoryProvider<RecordingFactory<String, ?>> provider = FactoryProvider.make(
 *      generators,
 *      FactoryProvider.DefaultKey.DEFAULT_KEY,
 *      new RecordingFactoryMaker(),
 *      true);
 * Address foo = provider.factory(Address.class).generate();
 * }</pre>
 *
 * @param <F> the type of factories held by this provider
 */
public class FactoryProvider<F extends Factory<String, ?>> {
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

    private Map<Class<?>, F> factories;

    public FactoryProvider(Map<Class<?>, F> factories) {
        this.factories = factories;
    }

    public Map<Class<?>, F> getFactories() {
        return factories;
    }

    public <T> Factory<String, T> factory(Class<T> clazz) {
        return (Factory<String, T>) factories.get(clazz);
    }

    /**
     * Create a FactoryProvider with the specified elements.
     *
     * @param generators             the generators to regroup
     * @param keyPolicy              the policy to use for the generation of default keys
     * @param factoryMaker           a factory maker that will regroup similar generators together into a factory
     * @param replaceInnerReferences true if the generators should be modified to use the new factories internally
     *                               (useful for recording factories and integration testing)
     * @param <F>                    the type of factory generated
     * @return a new Factory provider
     * @throws IllegalAccessException if replaceInnerReference is true and a security manager prevents reflective access to generator members.
     */
    public static <F extends Factory<String, ?>> FactoryProvider<F> make(Collection<Generator<?>> generators,
                                                                         DefaultKey keyPolicy,
                                                                         FactoryMaker<F> factoryMaker,
                                                                         boolean replaceInnerReferences) throws IllegalAccessException {
        Map<Class<?>, SortedSet<Generator<?>>> types = MetaGeneratorUtil.hierarchise(generators);
        Map<Class<?>, F> factories = new HashMap<>();
        for (Map.Entry<Class<?>, SortedSet<Generator<?>>> generatorsOfType : types.entrySet()) {
            Generator<?> defaultGenerator = generatorsOfType.getValue().first();
            String defaultKey = computeDefaultKey(keyPolicy, defaultGenerator);
            F factory = factoryMaker.factorise(generatorsOfType.getValue(),
                    generatorsOfType.getKey(),
                    Pair.<String, Generator<?>>of(defaultKey, defaultGenerator));
            factories.put(generatorsOfType.getKey(), factory);
        }

        if (replaceInnerReferences) {
            replaceInnerReferences(generators, factories);
        }

        return new FactoryProvider<>(factories);
    }

    /**
     * Replace the inner generators within given generators to use the given factories when possible
     *
     * @param generators to analyze
     * @param factories  to replace with
     * @throws IllegalAccessException if a security manager prevents reflective access to generator members.
     */
    public static void replaceInnerReferences(Collection<Generator<?>> generators, Map<Class<?>, ? extends Factory<?, ?>> factories) throws IllegalAccessException {
        for (Generator<?> generator : generators) {
            Map<Class<?>, Field[]> fields = ReflectionUtil.getAllFields(generator.getClass());
            Map<Class<?>, Field[]> innerGenerators = ReflectionUtil.keepAssignableFrom(fields, Generator.class, false);
            for (Field[] classFields : innerGenerators.values()) {
                for (Field field : classFields) {
                    replaceContent(factories, generator, field);
                }
            }
        }
    }

    private static void replaceContent(Map<Class<?>, ? extends Factory<?, ?>> factories, Generator<?> generator, Field field) throws IllegalAccessException {
        boolean originalAccessibility = field.isAccessible();
        field.setAccessible(true);
        Generator<?> innerGenerator = (Generator<?>) field.get(generator);
        // Find the equivalent Factory
        Factory<?, ?> factory = factories.get(MetaGeneratorUtil.returnType(innerGenerator));
        if (factory != null) {
            for (Map.Entry<?, ? extends Generator<?>> candidate : factory.getGenerators().entrySet()) {
                if (candidate.getValue() == innerGenerator) {
                    field.set(generator, new DefaultSurrogate(factory, candidate.getKey()));
                }
            }
        }
        field.setAccessible(originalAccessibility);
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
                throw new IllegalArgumentException("Unrecognized keyPolicy! " + keyPolicy);
        }
        return result;
    }
}
