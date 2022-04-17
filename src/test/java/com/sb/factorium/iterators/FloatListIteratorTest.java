package com.sb.factorium.iterators;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FloatListIteratorTest {

    private static final Faker faker = new Faker();
    private static final float DELTA_TOLERANCE = 2e-8f;

    private float[] array;
    private FloatListIterator iterator;

    @Before
    public void setUp() {
        array = new float[5];
        for (int i = 0; i < array.length; i++) {
            array[i] = (float) faker.random().nextDouble();
        }
        iterator = new FloatListIterator(array);
    }

    @Test
    public void hasNext() {
        for (int i = 0; i < array.length; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void next() {
        for (float j : array) {
            assertEquals(j, iterator.next(), DELTA_TOLERANCE);
        }
    }

    @Test
    public void hasPrevious() {
        assertFalse(iterator.hasPrevious());
        for (int i = 0; i < array.length; i++) {
            iterator.next();
            assertTrue(iterator.hasPrevious());
        }
    }

    @Test
    public void previous() {
        iterator.setIndex(array.length);
        for (int i = array.length - 1; i >= 0; i--) {
            assertEquals(array[i], iterator.previous(), DELTA_TOLERANCE);
        }
    }

    @Test
    public void nextIndex() {
        for (int i = 0; i < array.length; i++) {
            assertEquals(i, iterator.nextIndex());
            iterator.next();
        }
    }

    @Test
    public void previousIndex() {
        final int LAST_INDEX = array.length - 1;
        for (int i = -1; i < LAST_INDEX; i++) {
            assertEquals(i, iterator.previousIndex());
            iterator.next();
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void remove() {
        iterator.next();
        iterator.remove();
    }

    @Test
    public void set() {
        float setTo = (float) faker.random().nextDouble();
        iterator.next();
        iterator.set(setTo);
        assertEquals(setTo, iterator.previous(), DELTA_TOLERANCE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add() {
        iterator.add(0f);
    }

    /**
     * According to the documentation of ListIterator, chaining next-previous operations should return the same result.
     */
    @Test
    public void nextPreviousChain() {
        assertEquals(iterator.next(), iterator.previous());
    }
}