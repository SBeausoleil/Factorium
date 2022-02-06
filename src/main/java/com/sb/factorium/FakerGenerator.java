package com.sb.factorium;

import com.github.javafaker.Faker;

/**
 * BaseGenerator with a static Faker instance for quick use.
 * @param <T> the type generated by this generator.
 */
public abstract class FakerGenerator<T> extends BaseGenerator<T> {

    public static final Faker faker = new Faker();

}
