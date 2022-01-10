package com.sb.factorium;

import java.lang.reflect.Field;

/**
 * Implement the Modifier interface by modifying a field directly.
 */
public class FieldMod implements Modifier {
    protected final String field;
    protected final Object value;

    public FieldMod(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public void apply(Object target) {
        try {
            Field toChange = target.getClass().getDeclaredField(field);
            boolean originalAccess = toChange.isAccessible();
            toChange.setAccessible(true);
            toChange.set(target, value);
            toChange.setAccessible(originalAccess);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
