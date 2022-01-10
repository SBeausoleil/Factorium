package com.sb.factorium;

import com.sb.factorium.beans.Person;
import com.sb.factorium.generators.PersonGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RecordingFactoryTest {

    private RecordingFactory<String, Person> personFactory;

    @Before
    public void setUp() {
        Map<String, Generator<Person>> generators = new HashMap<>();
        generators.put(PersonGenerator.KEY, new PersonGenerator());
        personFactory = new RecordingFactory<>(PersonGenerator.KEY, generators);
    }

    @Test
    public void testNumberOfRecords() {
        final int N_ITEMS = 5;
        personFactory.generate(N_ITEMS);
        assertEquals(N_ITEMS, personFactory.getCreated().size());
        personFactory.generate();
        assertEquals(N_ITEMS + 1, personFactory.getCreated().size());
    }
}
