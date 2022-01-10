package com.sb.factorium;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic Generator implementation that handles the modifier use cases.
 * @param <T>
 */
public abstract class BaseGenerator<T> implements Generator<T> {

    /**
     * Generate a T item.
     * @return a T item.
     */
    protected abstract T make();

    @Override
    public T generate(Modifier... modifiers) {
        T result = make();
        for (Modifier modifier : modifiers) {
            modifier.apply(result);
        }
        return result;
    }

    @Override
    public List<T> generate(int nItems, Modifier... modifiers) {
        ArrayList<T> result = new ArrayList<>(nItems);
        for (int i = 0; i < nItems; i++) {
            result.add(generate(modifiers));
        }
        return result;
    }
}
