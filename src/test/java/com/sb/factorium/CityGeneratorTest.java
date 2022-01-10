package com.sb.factorium;

import com.sb.factorium.beans.City;
import com.sb.factorium.generators.CityGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

public class CityGeneratorTest {

    private CityGenerator generator = new CityGenerator();

    @Test
    public void testNoNullField() {
        City city = generator.generate();
        assertNotNull(city);
        assertNotNull(city.getName());
        assertTrue(city.getnCitizens() > 0);
    }
}