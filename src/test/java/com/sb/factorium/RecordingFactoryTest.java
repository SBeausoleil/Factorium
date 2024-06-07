package com.sb.factorium;

import com.sb.factorium.beans.Person;
import com.sb.factorium.generators.AddressGenerator;
import com.sb.factorium.generators.CityGenerator;
import com.sb.factorium.generators.PersonGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RecordingFactoryTest {

    private static RecordingFactory<String, Person> personFactory;

    @BeforeAll
    public static void setUp() {
        Map<String, Generator<Person>> generators = new HashMap<>();
        CityGenerator cityGenerator = new CityGenerator();
        AddressGenerator addressGenerator = new AddressGenerator(cityGenerator);
        generators.put(PersonGenerator.KEY, new PersonGenerator(cityGenerator, addressGenerator));
        personFactory = new RecordingFactory<>(Person.class, PersonGenerator.KEY, generators);
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
