package com.sb.factorium;

import java.util.List;
import java.util.function.Consumer;

/**
 * Generator for an Object type used by Factorium.
 * @param <T> the generated object's type.
 */
public interface Generator<T> {
    /**
     * Generate an item
     * @param modifiers the modifiers to apply on the items/
     * @return one object of the generator's target type.
     */
    T generate(Modifier... modifiers);

    /**
     * Generate an item.
     *
     * Note that the first lambdaMod is applied AFTER the modifiers.
     * @param lambdaMod a lambda to modify the returned item after the optional modifiers
     * @param modifiers the modifiers to apply first on the items/
     * @return one object of the generator's target type.
     */
    default T generate(Consumer<T> lambdaMod, Modifier... modifiers) {
        T val = generate(modifiers);
        lambdaMod.accept(val);
        return val;
    }

    /**
     * Generate nItems
     * @param nItems the number of items to generate.
     * @param modifiers the modifiers to apply on the items.
     * @return a list containing all the generated items. If nItems is zero, return an empty (modifiable) list.
     */
    List<T> generate(int nItems, Modifier... modifiers);

    /**
     * Generate nItems
     *
     * Note that the first lambdaMod is applied AFTER the modifiers.
     * @param nItems the number of items to generate.
     * @param lambdaMod a lambda to modify the returned items after the optional modifiers
     * @param modifiers the modifiers to apply on the items.
     * @return a list containing all the generated items. If nItems is zero, return an empty (modifiable) list.
     */
    default List<T> generate(int nItems, Consumer<T> lambdaMod, Modifier... modifiers) {
        List<T> vals = generate(nItems, modifiers);
        vals.forEach(lambdaMod);
        return vals;
    }
}
