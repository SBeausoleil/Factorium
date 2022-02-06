package com.sb.factorium.reflection;

import java.lang.reflect.Field;

/**
 * Test if the given field can be assigned to the target type.
 */
public class TestAssignableTo implements Condition<Field> {
    private final Class<?> target;

    public TestAssignableTo(Class<?> target) {
        this.target = target;
    }

    @Override
    public boolean test(Field field) {
        return target.isAssignableFrom(field.getType());
    }

    public Class<?> getTarget() {
        return target;
    }
}
