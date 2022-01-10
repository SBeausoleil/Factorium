package com.sb.factorium;

import com.sb.factorium.beans.City;

public class LambdaTest {

    public <T> T foo(Generator<T> generator) {
        return generator.generate();
    }

    public void test() {
        foo(() -> new City("", 1234));
    }
}
