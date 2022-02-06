package com.sb.factorium.reflection;

/**
 * Objects implementing Condition can describe a condition.
 *
 * Similar to Predicate in Java 8.
 * @param <T> the type of objects accepted by this condition.
 */
public interface Condition<T> {
    boolean test(T obj);
}
