package com.sb.factorium;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

/**
 * Implement the Modifier interface by modifying a field directly.
 *
 * Supports nested targets by separating each field name with a dot.
 *
 * e.g.: (on an Address) "city.nCitizens"
 */
public class FieldMod implements Modifier {
    private final String[] nestedFields;
    private final Object value;

    public FieldMod(String field, Object value) {
        this.value = value;

        nestedFields = field.split("\\.");
    }

    @Override
    public void apply(Object target) {
        try {
            String field = nestedFields[0];
            if (nestedFields.length > 1) {
                // Iteratively get the real target
                target = findRealTarget(target);
                field = nestedFields[nestedFields.length - 1];
            }


            Field toChange = target.getClass().getDeclaredField(field);
            boolean originalAccess = toChange.isAccessible();
            toChange.setAccessible(true);
            toChange.set(target, value);
            toChange.setAccessible(originalAccess);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private Object findRealTarget(Object target) throws NoSuchFieldException, IllegalAccessException {
        for (int i = 0; i < nestedFields.length - 1; i++) {
            Field targetField = target.getClass().getDeclaredField(nestedFields[i]);
            boolean originalAccess = targetField.isAccessible();
            targetField.setAccessible(true);
            target = targetField.get(target);
            targetField.setAccessible(originalAccess);
        }
        return target;
    }

    /**
     * Return the path to the target field.
     * @return the path to the target field.
     */
    public String fullTarget() {
        return StringUtils.joinWith(".", nestedFields);
    }

    public Object getValue() {
        return value;
    }
}
