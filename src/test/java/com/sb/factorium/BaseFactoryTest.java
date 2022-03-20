package com.sb.factorium;

import com.sb.factorium.beans.City;
import com.sb.factorium.generators.CityGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BaseFactoryTest {

    private static class DummyFactory<String, T> extends BaseFactory<String, T> {

        int decorated;

        public DummyFactory(Class<T> generatedType, String defaultKey, Map<String, Generator<T>> generators) {
            super(generatedType, defaultKey, generators);
            decorated = 0;
        }

        @Override
        protected void decorate(T newItem, Modifier[] modifiers) {
            decorated++;
        }
    }

    private Map<String, Generator<City>> generators;
    private CityGenerator cityGenerator;

    @Before
    public void setUp() throws Exception {
        generators = new HashMap<>();
        cityGenerator = new CityGenerator();
        generators.put("city", cityGenerator);
    }

    @Test()
    public void testConstructor() {
        BaseFactory<String, City> factory = new DummyFactory<>(City.class, "city", generators);
        assertEquals("city", factory.getDefaultKey());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_absentKey() {
        new DummyFactory<>(City.class, "foo", generators);
    }

    @Test
    public void testDecorate_Collection() {
        DummyFactory<String, City> factory = new DummyFactory<>(City.class, "city", generators);
        final int N_CITIES = 5;
        List<City> cities = cityGenerator.generate(N_CITIES);
        factory.decorate(cities, null);
        assertEquals(N_CITIES, factory.decorated);
    }

    @Test
    public void testGetGeneratedType() {
        BaseFactory<String, City> factory = new DummyFactory<>(City.class, "city", generators);
        assertEquals(City.class, factory.getGeneratedType());
    }
}