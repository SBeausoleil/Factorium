package com.sb.factorium.iterators;

import com.github.javafaker.Faker;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ByteListIteratorTest {
    private static final Faker faker = new Faker();

    private byte[] array;
    private ByteListIterator iterator;

    @Before
    public void setUp() {
        array = new byte[5];
        for (int i = 0; i < array.length; i++) {
            array[i] = (byte) (int) faker.random().nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
        }
        iterator = new ByteListIterator(array);
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
        for (int j : array) {
            assertEquals(j, (int) iterator.next());
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
            assertEquals(array[i], (int) iterator.previous());
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
        byte setTo = (byte) (int) faker.random().nextInt(Byte.MIN_VALUE, Byte.MAX_VALUE);
        iterator.next();
        iterator.set(setTo);
        assertEquals(setTo, (byte) iterator.previous());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add() {
        iterator.add((byte) 0);
    }

    /**
     * According to the documentation of ListIterator, chaining next-previous operations should return the same result.
     */
    @Test
    public void nextPreviousChain() {
        assertEquals(iterator.next(), iterator.previous());
    }
}