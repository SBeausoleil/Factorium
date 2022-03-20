package com.sb.factorium;

import java.util.*;

public final class RandomUtil {
    private static WeakHashMap<Class<?>, Object[]> enumsValuesCache = new WeakHashMap<>();

    private static Random rng = new Random();

    private RandomUtil() {}

    public static <T extends Enum> T randomEnum(Class<T> enumClass) {
        @SuppressWarnings("unchecked") // It is impossible to check the type of object vs generic
        T[] values = (T[]) enumsValuesCache.get(enumClass);
        if (values == null) {
            values = enumClass.getEnumConstants();
            enumsValuesCache.put(enumClass, values);
        }
        return values[rng.nextInt(values.length)];
    }

    public static byte randomElement(byte[] array) {
        return array[rng.nextInt(array.length)];
    }

    public static char randomElement(char[] array) {
        return array[rng.nextInt(array.length)];
    }

    public static short randomElement(short[] array) {
        return array[rng.nextInt(array.length)];
    }

    public static int randomElement(int[] array) {
        return array[rng.nextInt(array.length)];
    }

    public static long randomElement(long[] array) {
        return array[rng.nextInt(array.length)];
    }

    public static float randomElement(float[] array) {
        return array[rng.nextInt(array.length)];
    }

    public static double randomElement(double[] array) {
        return array[rng.nextInt(array.length)];
    }

    public static <T> T randomElement(T[] array) {
        return array[rng.nextInt(array.length)];
    }

    public static <T> T randomElement(List<T> list) {
        return list.get(rng.nextInt(list.size()));
    }

    public static <T> T randomElement(Collection<T> collection) {
        final int target = rng.nextInt(collection.size());
        Iterator<T> it = collection.iterator();
        for (int i = 0; i < target; i++) {
            it.next();
        }
        return it.next();
    }
}
