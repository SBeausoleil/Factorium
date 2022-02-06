package com.sb.factorium;

import org.apache.commons.lang3.ClassUtils;

import java.util.Comparator;
import java.util.Iterator;
import java.util.WeakHashMap;

/**
 * A Comparator that compares using the distance between the given classes and its target class.
 * If a given class does not have the target in its hierarchy, an exception will be raised.
 *
 * This comparator can be configured to never return equal (0). In such a case that two classes
 * are equidistant from the target, they will be compared lexicographically using their name.
 */
public class ClassDistanceComparator implements Comparator<Class<?>> {

    private Class<?> target;
    private final boolean allowEquals;

    private final WeakHashMap<Class<?>, Integer> distancesCache;

    public ClassDistanceComparator(Class<?> target, boolean allowEquals) {
        this.target = target;
        this.allowEquals = allowEquals;
        distancesCache = new WeakHashMap<>();
    }

    @Override
    public int compare(Class<?> left, Class<?> right) {
        int result = Integer.compare(distance(left), distance(right));
        if (result == 0 && !allowEquals) {
            result = left.getName().compareTo(right.getName());
        }
        return result;
    }

    /**
     * Calculate the distance between a given class and the target of this comparator.
     *
     * @param clazz to analyze
     * @return the distance to the target class
     * @throws IllegalArgumentException if clazz does not hold the target in its hierarchy.
     */
    public int distance(Class<?> clazz) {
        Integer distance = distancesCache.get(clazz);
        if (distance == null) {
            int dist = 0;
            ClassUtils.Interfaces interfaceChecking = target.isInterface() ?
                    ClassUtils.Interfaces.INCLUDE : ClassUtils.Interfaces.EXCLUDE;
            Iterator<Class<?>> hierarchyIt = ClassUtils.hierarchy(clazz, interfaceChecking).iterator();
            while (hierarchyIt.hasNext()) {
                dist++;
                Class<?> superClass = hierarchyIt.next();
                if (superClass.equals(target)) {
                    break;
                }
            }
            if (hierarchyIt.hasNext()) {
                throw new IllegalArgumentException(clazz.getName() + " is not a subclass of " + target.getName());
            }
            distance = dist;
            distancesCache.put(clazz, distance);
        }
        return distance;
    }

    public Class<?> getTarget() {
        return target;
    }

    public boolean isAllowEquals() {
        return allowEquals;
    }
}
