package com.sb.factorium;

import org.apache.commons.lang3.ClassUtils;

import java.util.*;

public final class MetaGeneratorUtil {

    private static WeakHashMap<Generator<?>, Class<?>> returnTypesCache = new WeakHashMap<>();

    private static boolean tolerateUnknownAutoPersist = true;

    private MetaGeneratorUtil() {
    }

    public static boolean tolerateUnknownAutoPersist() {
        return tolerateUnknownAutoPersist;
    }

    public static void tolerateUnknownAutoPersist(boolean tolerateUnknownAutoPersist) {
        MetaGeneratorUtil.tolerateUnknownAutoPersist = tolerateUnknownAutoPersist;
    }

    /**
     * Suggest a name for a generator class.
     * First checks for the GeneratorInfo annotation.
     * If it isn't present, use the name of the class (from getName()).
     *
     * @param generator to inspect
     * @return a suggested name for this generator.
     */
    public static String suggestName(Generator<?> generator) {
        Class<?> clazz = generator.getClass();
        GeneratorInfo meta = clazz.getAnnotation(GeneratorInfo.class);
        if (meta != null)
            return meta.name();
        return clazz.getName();
    }

    /**
     * Get the type of objects created by the given generator.
     * First checks the GeneratorInfo annotation.
     * If it is not found, and we tolerate unknown auto-persistence, generate an object with it and return its type.
     *
     * @param generator to inspect
     * @return the class of objects created by the given generator.
     * @throws IllegalArgumentException if the generator does not have the #GeneratorInfo annotation, and we do not tolerate unknown auto-persistence.
     */
    public static Class<?> returnType(Generator<?> generator) {
        Class<?> result = returnTypesCache.get(generator);
        if (result == null) {
            Class<?> clazz = generator.getClass();
            GeneratorInfo meta = clazz.getAnnotation(GeneratorInfo.class);
            if (meta != null && meta.target() != null) {
                result = meta.target();
            } else if ((meta == null && tolerateUnknownAutoPersist)
                    || (meta != null && !meta.autoPersist())) {
                result = generator.generate().getClass();
            } else {
                throw new IllegalArgumentException("The given generator " + suggestName(generator) + " does not specify a return type and " +
                        (meta == null ? "might " : "") + "auto-persists!");
            }
            returnTypesCache.put(generator, result);
        }
        return result;
    }

    /**
     * Analyse and hierarchise the generators in this map according to the type variances of their target types.
     *
     * @param generators to analyse
     * @return the hierarchy of classes and generators for these classes.
     * The first generator for each class should be considered its default one.
     */
    public static Map<Class<?>, SortedSet<Generator<?>>> hierarchise(Collection<Generator<?>> generators) {
        Map<Class<?>, SortedSet<Generator<?>>> hierarchy = new HashMap<>();
        for (Generator<?> generator : generators) {
            for (Class<?> clazz : ClassUtils.hierarchy(returnType(generator), ClassUtils.Interfaces.INCLUDE)) {
                SortedSet<Generator<?>> generatorsForClass = hierarchy.get(clazz);
                if (generatorsForClass == null) {
                    generatorsForClass = new TreeSet<>(new GeneratorComparator(clazz));
                    hierarchy.put(clazz, generatorsForClass);
                }
                generatorsForClass.add(generator);
            }
        }
        return hierarchy;
    }

    /**
     * Verify if all the given generators produce items that may be assigned to the given type.
     * @param generators to check
     * @param target assigned to
     * @return true if all the generators can assign their output to the target type.
     */
    public static boolean allAssignableTo(Collection<Generator<?>> generators, Class<?> target) {
        for (Generator<?> generator : generators) {
            if (!target.isAssignableFrom(returnType(generator))) {
                return false;
            }
        }
        return true;
    }
}
