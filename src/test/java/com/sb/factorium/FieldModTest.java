package com.sb.factorium;

import com.sb.factorium.beans.City;
import com.sb.factorium.generators.CityGenerator;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class FieldModTest {

    @Test
    public void testPrivateField() {
        final long EXPECTED_RESULT = new Random().nextLong();
        City city = new CityGenerator().generate(new FieldMod("nCitizens", EXPECTED_RESULT));
        assertEquals(EXPECTED_RESULT, city.getnCitizens());
    }
}