package com.sb.factorium.iterators;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class IntListIterator implements ListIterator<Integer> {

    private final int[] array;
    private int index;

    public IntListIterator(int[] array) {
        this.array = array;
        this.index = -1;
    }

    @Override
    public boolean hasNext() {
        return index < array.length - 1;
    }

    @Override
    public Integer next() {
        if (hasNext())
            return array[++index];
        throw new NoSuchElementException();
    }

    @Override
    public boolean hasPrevious() {
        return index >= 0;
    }

    @Override
    public Integer previous() {
        if (index == 0)
            return array[index--];
        if (hasPrevious())
            return array[--index];
        throw new NoSuchElementException();
    }

    @Override
    public int nextIndex() {
        return index + 1;
    }

    @Override
    public int previousIndex() {
        return index;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can't remove an element of a primitive array!");
    }

    @Override
    public void set(Integer val) {
        array[index] = val;
    }

    @Override
    public void add(Integer val) {
        throw new UnsupportedOperationException("Can't add an element to a primitive array!");
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
