package com.sb.factorium.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

public final class ReflectionUtil {
    private ReflectionUtil() {
    }

    private static WeakHashMap<Class<?>, Map<Class<?>, Field[]>> cache = new WeakHashMap<>();

    /**
     * Get all the fields that are present in members of a given class.
     * Recursively checks up into the class tree of clazz to accumulate members.
     *
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

    /**
     * Keep fields that can assign their value to the target class.
     *
     * @param fields
     * @param target
     * @return a map of classes and their respective fields that can assign their content to the given target.
     */
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

    public static Field[] keepAssignableFrom(Field[] fields, Class<?> target, boolean keepFinal) {
        ArrayList<Field> toKeep = new ArrayList<>();
        for (Field field : fields) {
            if (field.getType().isAssignableFrom(target)) {
                if (!keepFinal && Modifier.isFinal(field.getModifiers()))
                    continue;
                toKeep.add(field);
            }
        }
        return toKeep.toArray(new Field[0]);
    }

    public static Map<Class<?>, Field[]> keepAssignableFrom(Map<Class<?>, Field[]> fields, Class<?> target, boolean keepFinal) {
        Map<Class<?>, Field[]> result = new LinkedHashMap<>();
        for (Map.Entry<Class<?>, Field[]> classFields : fields.entrySet()) {
            result.put(classFields.getKey(), keepAssignableFrom(classFields.getValue(), target, keepFinal));
        }
        return result;
    }

    public static Field[] removeFieldsOfClass(Field[] fields, Class<?> toRemove) {
        ArrayList<Field> toKeep = new ArrayList<>(fields.length);
        for (Field field : fields) {
            if (!field.getType().equals(toRemove)) {
                toKeep.add(field);
            }
        }
        return toKeep.toArray(new Field[0]);
    }

    public static Map<Class<?>, Field[]> removeFieldsOfClass(Map<Class<?>, Field[]> fields, Class<?> toRemove) {
        Map<Class<?>, Field[]> result = new LinkedHashMap<>();
        for (Map.Entry<Class<?>, Field[]> classFields : fields.entrySet()) {
            result.put(classFields.getKey(), removeFieldsOfClass(classFields.getValue(), toRemove));
        }
        return result;
    }

    /**
     * Check if one of the given classes is enclosing the other.
     * @param left left class
     * @param right right class
     * @return -1 if the left one encloses the right one
     *          0 if none are enclosing the other
     *          1 if the right one encloses the left one
     */
    public static int checkEnclosing(Class<?> left, Class<?> right) {
        if (isEnclosedBy(right, left))
            return -1;
        else if (isEnclosedBy(left, right))
            return 1;
        return 0;
    }

    /**
     * Check if the inner class is enclosed by the outer class.
     * @param inner potential inner class
     * @param outer potential outer class
     * @return true if the inner class is nested within the outer at some level.
     */
    public static boolean isEnclosedBy(Class<?> inner, Class<?> outer) {
        Class<?> current = inner;
        Class<?> enclosing;
        do {
            enclosing = current.getEnclosingClass();
            if (outer.equals(enclosing))
                return true;
            current = enclosing;
        } while (enclosing != null);
        return false;
    }
}
