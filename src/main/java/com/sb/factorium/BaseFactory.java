package com.sb.factorium;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class BaseFactory<K, T> implements Factory<K, T> {

    private final Class<T> generatedType;
    protected K defaultKey;
    protected Map<K, Generator<T>> generators;


    protected BaseFactory(Class<T> generatedType, K defaultKey, Map<K, Generator<T>> generators) {
        if (!generators.containsKey(defaultKey)) {
            throw new IllegalArgumentException("The default key is not present in the generators map.");
        }
        this.generatedType = generatedType;
        this.defaultKey = defaultKey;
        this.generators = generators;
    }

    @Override
    public Map<K, Generator<T>> getGenerators() {
        return generators;
    }


    @Override
    public T generate(Modifier... modifiers) {
        return generate(defaultKey, modifiers);
    }

    @Override
    public List<T> generate(int nItems, Modifier... modifiers) {
        return generate(defaultKey, nItems, modifiers);
    }


    @Override
    public T generate(K generatorKey, Modifier... modifiers) {
        Generator<T> generator = getGenerator(generatorKey);
        T result = generator.generate(modifiers);
        decorate(result, modifiers);
        return result;
    }

    @Override
    public List<T> generate(K generatorKey, int nItems, Modifier... modifiers) {
        Generator<T> generator = getGenerator(generatorKey);
        List<T> results = generator.generate(nItems, modifiers);
        decorate(results, modifiers);
        return results;
    }

    protected Generator<T> getGenerator(K generatorKey) {
        Generator<T> generator = generators.get(generatorKey);
        if (generator == null) {
            throw new NoSuchElementException("No registered generator with the key " + generatorKey);
        }
        return generator;
    }

    /**
     * Decorate the given item.
     * @param newItem the created item.
     * @param modifiers the modifiers applied to it.
     */
    protected abstract void decorate(T newItem, Modifier[] modifiers);

    /**
     * List decorator.
     * Used in the case of multiple generations. By default just calls decorate(T, Modifier[]) for each item.
     * @param newItems the list of created items
     * @param modifiers the modifiers applied to them.
     */
    protected void decorate(Collection<T> newItems, Modifier[] modifiers) {
        for (T obj : newItems) {
            decorate(obj, modifiers);
        }
    }

    @Override
    public K getDefaultKey() {
        return defaultKey;
    }

    public Class<T> getGeneratedType() {
        return generatedType;
    }
}
