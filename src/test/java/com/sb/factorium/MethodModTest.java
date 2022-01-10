package com.sb.factorium;

import com.sb.factorium.beans.City;
import com.sb.factorium.generators.CityGenerator;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class MethodModTest {

    @Test
    public void testUsePublicMethod() {
        final long EXPECTED_RESULT = new Random().nextLong();
        City city = new CityGenerator().generate(new MethodMod("setnCitizens", EXPECTED_RESULT));
        assertEquals(EXPECTED_RESULT, city.getnCitizens());
    }

    @Test
    public void testUserPrivateMethod() {
        final long EXPECTED_RESULT = new Random().nextLong();
        City city = new CityGenerator().generate(new MethodMod("privateSetterForCitizens", EXPECTED_RESULT));
        assertEquals(EXPECTED_RESULT, city.getnCitizens());
    }

    @Test
    public void testNoMethodFound() {
        final long EXPECTED_RESULT = new Random().nextLong();
        try {
            new CityGenerator().generate(new MethodMod("blashblasdhasd", EXPECTED_RESULT));
            fail();
        } catch (RuntimeException e) {
            assertEquals(NoSuchMethodException.class, e.getCause().getClass());
        }
    }
}