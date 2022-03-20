package com.sb.factorium;

import com.github.javafaker.Faker;
import com.sb.factorium.beans.Address;
import com.sb.factorium.beans.City;
import com.sb.factorium.generators.AddressGenerator;
import com.sb.factorium.generators.CityGenerator;
import org.junit.Test;

import static org.junit.Assert.*;

public class MixedMultiModTest {
    private Faker faker = new Faker();
    private CityGenerator cityGenerator = new CityGenerator();
    private AddressGenerator addressGenerator = new AddressGenerator(cityGenerator);

    @Test
    public void testInitialization() {
        final String postalCode = faker.bothify("?#?");
        final City shortSetterValue = cityGenerator.generate();
        final City longSetterValue = cityGenerator.generate();
        final int streetAddress = faker.random().nextInt(1000);
        final String streetName = faker.letterify("??????");
        final long nCitizens = faker.random().nextLong();
        MixedMultiMod mod = new MixedMultiMod(
                "postalCode", postalCode,
                "setCity()", shortSetterValue,
                "setCity(1)", longSetterValue,
                "setNumberAndStreet(2)", streetAddress, streetName,
                "city.nCitizens", nCitizens);

        FieldMod simpleFieldMod = (FieldMod) mod.modifiers[0];
        assertEquals("postalCode", simpleFieldMod.fullTarget());
        assertEquals(postalCode, simpleFieldMod.getValue());

        MethodMod shortMethodMod = (MethodMod) mod.modifiers[1];
        assertEquals("setCity", shortMethodMod.method);
        assertEquals(shortSetterValue, shortMethodMod.arguments[0]);

        MethodMod longMethodMod = (MethodMod) mod.modifiers[2];
        assertEquals("setCity", longMethodMod.method);
        assertEquals(longSetterValue, longMethodMod.arguments[0]);

        MethodMod complexMethodMod = (MethodMod) mod.modifiers[3];
        assertEquals("setNumberAndStreet", complexMethodMod.method);
        assertEquals(streetAddress, complexMethodMod.arguments[0]);
        assertEquals(streetName, complexMethodMod.arguments[1]);

        FieldMod nestedFieldMod = (FieldMod) mod.modifiers[4];
        assertEquals("postalCode", simpleFieldMod.fullTarget());
        assertEquals(nCitizens, nestedFieldMod.getValue());
    }

    @Test
    public void testApply() {
        final String postalCode = faker.bothify("?#?");
        final City city = cityGenerator.generate();
        final int streetAddress = faker.random().nextInt(1000);
        final String streetName = faker.letterify("??????");
        final long nCitizens = faker.random().nextLong();
        MixedMultiMod mod = new MixedMultiMod(
                "postalCode", postalCode,
                "setCity()", city,
                "setNumberAndStreet(2)", streetAddress, streetName,
                "city.nCitizens", nCitizens);

        Address address = addressGenerator.generate(mod);
        assertEquals(postalCode, address.getPostalCode());
        assertEquals(city, address.getCity());
        assertEquals(streetAddress, address.getNumber());
        assertEquals(streetName, address.getStreetName());
        assertEquals(nCitizens, address.getCity().getnCitizens());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitExceptions_notEnoughArguments_0() {
        new MixedMultiMod();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInitExceptions_notEnoughArguments_1() {
        new MixedMultiMod("Not enough");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInitExceptions_missingFieldValue() {
        new MixedMultiMod("Padding", "Padding", "field");
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInitExceptions_notEnoughArgumentsForComplexMethod() {
        new MixedMultiMod("complexMethod(3)", "one", "two");
    }
}