package com.sb.factorium.reflection;

import com.sb.factorium.BaseGenerator;
import com.sb.factorium.Factory;

public class DefaultSurrogate<K, T> extends BaseGenerator {
    private final Factory<K, T> factory;
    private final K key;

    public DefaultSurrogate(Factory<K, T> factory, K key) {
        this.factory = factory;
        this.key = key;
    }

    @Override
    protected Object make() {
        return factory.generate(key);
    }
}
