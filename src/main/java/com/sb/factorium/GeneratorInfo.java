package com.sb.factorium;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation that when attached to a generator helps provide additional information about it.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface GeneratorInfo {
    /**
     * The suggested name of this generator.
     */
    public String name();

    /**
     * What this generator creates.
     */
    public Class<?> target();

    /**
     * Should this generator be considered the default generator for its target?
     */
    public boolean isDefault() default false;

    /**
     * Does generating an object with this generator automatically persists it?
     */
    public boolean autoPersist() default false;
}
