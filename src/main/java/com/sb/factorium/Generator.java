package com.sb.factorium;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Generator for an Object type used by Factorium.
 * @param <T> the generated object's type.
 */
@FunctionalInterface
public interface Generator<T> extends Supplier<T> {
    /**
     * Generate an item
     * @param  modifiers the modifiers to apply on the items/
     * @return one object of the generator's target type.
     */
    default T generate(Modifier... modifiers) {
        T result = get();
        for (Modifier modifier : modifiers) {
            modifier.apply(result);
        }
        return result;
    }

    /**
     * Generate nItems
     * @param nItems the number of items to generate.
     * @param modifiers the modifiers to apply on the items.
     * @return a list containing all the generated items. If nItems is zero, return an empty (modifiable) list.
     */
    default List<T> generate(int nItems, Modifier... modifiers) {
        ArrayList<T> result = new ArrayList<>(nItems);
        for (int i = 0; i < nItems; i++) {
            result.add(generate(modifiers));
        }
        return result;
    }
}
