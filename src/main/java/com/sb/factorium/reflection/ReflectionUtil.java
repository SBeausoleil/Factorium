package com.sb.factorium.reflection;

import java.lang.reflect.Field;
import java.util.*;

public final class ReflectionUtil {
    private ReflectionUtil() {}

    private static WeakHashMap<Class<?>, Map<Class<?>, Field[]>> cache = new WeakHashMap<>();

    /**
     * Get all the fields that are present in members of a given class.
     * Recursively checks up into the class tree of clazz to accumulate members.
     * @param clazz to analyze
     * @return all the fields that members of clazz have.
     */
    public static Map<Class<?>, Field[]> getAllFields(Class<?> clazz) {
        Map<Class<?>, Field[]> fields = cache.get(clazz);
        if (fields == null) {
            fields = new HashMap<>();
            fields.put(clazz, clazz.getDeclaredFields());
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                fields.putAll(getAllFields(superClass));
            }
            cache.put(clazz, fields);
        }
        return fields;
    }

    public static Map<Class<?>, Field[]> keepAssignableTo(Map<Class<?>, Field[]> fields, Class<?> target) {
        Map<Class<?>, Field[]> result = new HashMap<>();
        for (Map.Entry<Class<?>, Field[]> entry : fields.entrySet()) {
            ArrayList<Field> toKeep = new ArrayList<>();
            for (Field field : entry.getValue()) {
                if (target.isAssignableFrom(field.getType())) {
                    toKeep.add(field);
                }
            }
            if (!toKeep.isEmpty()) {
                result.put(entry.getKey(), toKeep.toArray(new Field[0]));
            }
        }
        return result;
    }
}
