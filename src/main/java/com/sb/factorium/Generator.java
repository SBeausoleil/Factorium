package com.sb.factorium;

import java.util.List;

/**
 * Generator for an Object type used by Factorium.
 * @param <T> the generated object's type.
 */
public interface Generator<T> {
    /**
     * Generate an item
     * @param  modifiers the modifiers to apply on the items/
     * @return one object of the generator's target type.
     */
    T generate(Modifier... modifiers);

    /**
     * Generate nItems
     * @param nItems the number of items to generate.
     * @param modifiers the modifiers to apply on the items.
     * @return a list containing all the generated items. If nItems is zero, return an empty (modifiable) list.
     */
    List<T> generate(int nItems, Modifier... modifiers);
}
