package com.sb.factorium.iterators;

import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DoubleListIteratorTest {

    private static final Faker faker = new Faker();
    private static final double DELTA_TOLERANCE = 2e-15f;

    private double[] array;
    private DoubleListIterator iterator;

    @BeforeEach
    public void setUp() {
        array = new double[5];
        for (int i = 0; i < array.length; i++) {
            array[i] = faker.random().nextDouble();
        }
        iterator = new DoubleListIterator(array);
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
        for (double j : array) {
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

    @Test
    public void remove() {
        assertThrows(UnsupportedOperationException.class, () -> {
            iterator.next();
            iterator.remove();
        });
    }

    @Test
    public void set() {
        double setTo = faker.random().nextDouble();
        iterator.next();
        iterator.set(setTo);
        assertEquals(setTo, iterator.previous(), DELTA_TOLERANCE);
    }

    @Test
    public void add() {
        assertThrows(UnsupportedOperationException.class, () -> iterator.add(0.0));
    }

    /**
     * According to the documentation of ListIterator, chaining next-previous operations should return the same result.
     */
    @Test
    public void nextPreviousChain() {
        assertEquals(iterator.next(), iterator.previous());
    }
}