package com.sb.factorium;

import com.sb.factorium.beans.Address;
import com.sb.factorium.beans.City;
import com.sb.factorium.generators.AddressGenerator;
import com.sb.factorium.generators.CityGenerator;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class FieldModTest {

    private CityGenerator cityGenerator;
    private AddressGenerator addressGenerator;

    @Before
    public void setUp() {
        cityGenerator = new CityGenerator();
        addressGenerator = new AddressGenerator(cityGenerator);
    }

    @Test
    public void testPrivateField() {
        final long EXPECTED_RESULT = new Random().nextLong();
        City city = cityGenerator.generate(new FieldMod("nCitizens", EXPECTED_RESULT));
        assertEquals(EXPECTED_RESULT, city.getnCitizens());
    }

    @Test
    public void testNestedFields() {
        final long EXPECTED_RESULT = new Random().nextLong();
        Address address = addressGenerator.generate(new FieldMod("city.nCitizens", EXPECTED_RESULT));
        assertEquals(EXPECTED_RESULT, address.getCity().getnCitizens());
    }
}