package com.sb.factorium;

import org.apache.commons.lang3.reflect.MethodUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Implement the Modifier interface by invoking a method on the target object.
 */
public class MethodMod implements Modifier {
    protected final String method;
    protected final Object[] arguments;
    protected final Class<?>[] types;

    public MethodMod(String method, Object... arguments) {
        this.method = method;
        this.arguments = arguments;
        types = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            types[i] = arguments[i].getClass();
        }
    }

    @Override
    public void apply(Object target) {
        try {
            Method use = MethodUtils.getMatchingMethod(target.getClass(), method, types);
            if (use == null) {
                throw new NoSuchMethodException("Method " + method + " could not be found in class " + target.getClass());
            }
            boolean accessibility = use.isAccessible();
            use.setAccessible(true);
            use.invoke(target, arguments);
            use.setAccessible(accessibility);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
