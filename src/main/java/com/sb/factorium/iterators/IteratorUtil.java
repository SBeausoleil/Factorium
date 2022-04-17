package com.sb.factorium.iterators;

import java.util.*;

public final class IteratorUtil {
    private IteratorUtil() {}

    public static ListIterator<?> toListIterator(Object obj) {
        if (obj instanceof List)
            return ((List<?>) obj).listIterator();
        if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().isPrimitive()) {
                return toPrimitiveListIterator(obj);
            }
            return Arrays.asList((Object[]) obj).listIterator();
        }
        throw new IllegalArgumentException(obj + " is neither a list or an array!");
    }

    public static ListIterator<?> toPrimitiveListIterator(Object array) {
        if (array.getClass() == boolean[].class)
            return new BooleanListIterator((boolean[]) array);
        if (array.getClass() == byte[].class)
            return new ByteListIterator((byte[]) array);
        if (array.getClass() == short[].class)
            return new ShortListIterator((short[]) array);
        if (array.getClass() == int[].class)
            return new IntListIterator((int[]) array);
        if (array.getClass() == long[].class)
            return new LongListIterator((long[]) array);
        if (array.getClass() == float[].class)
            return new FloatListIterator((float[]) array);
        if (array.getClass() == double[].class)
            return new DoubleListIterator((double[]) array);
        throw new IllegalArgumentException(array.getClass() + " is not a primitive array!");
    }

    public static Iterator<?> toIterator(Object obj) {
        if (obj instanceof Iterable)
            return ((Iterable<?>) obj).iterator();
        if (obj instanceof Map) {
            return ((Map<?,?>) obj).values().iterator();
        }
        if (obj.getClass().isArray()) {
            if (obj.getClass().getComponentType().isPrimitive()) {
                return toPrimitiveListIterator(obj);
            }
            return Arrays.asList((Object[]) obj).listIterator();
        } else {
            throw new IllegalArgumentException("Type " + obj.getClass().getName() + " is neither iterable, or an array!");
        }
    }
}
