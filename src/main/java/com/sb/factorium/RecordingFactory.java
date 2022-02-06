package com.sb.factorium;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * A RecordingFactory is a factory that keeps an accessible record of everything it has created.
 *
 * @param <T> the type of objects generated by this factory.
 */
public class RecordingFactory<K, T> extends BaseFactory<K, T> {

    private static final Vector<RecordingFactory<?,?>> pool = new Vector<>();

    protected List<T> created;

    public RecordingFactory(Class<T> generatedType, K defaultKey, Map<K, Generator<T>> generators) {
        super(generatedType, defaultKey, generators);
        this.created = new Vector<>();
        pool.add(this);
    }

    @Override
    protected void decorate(T newItem, Modifier[] modifiers) {
        created.add(newItem);
    }

    @Override
    protected void decorate(List<T> newItems, Modifier[] modifiers) {
        created.addAll(newItems);
    }

    public List<T> getCreated() {
        return created;
    }

    public static Vector<RecordingFactory<?,?>> getRecordingsPool() {
        return pool;
    }
}
