package com.sb.factorium;

import com.github.javafaker.Faker;
import com.sb.factorium.beans.*;
import com.sb.factorium.generators.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class FieldModTest {

    private Faker faker = new Faker();
    private CityGenerator cityGenerator;
    private AddressGenerator addressGenerator;
    private PersonGenerator personGenerator;
    private CitizensRegistryGenerator registryGenerator;
    private LotteryGenerator lotteryGenerator;

    @Before
    public void setUp() {
        cityGenerator = new CityGenerator();
        addressGenerator = new AddressGenerator(cityGenerator);
        personGenerator = new PersonGenerator(cityGenerator, addressGenerator);
        registryGenerator = new CitizensRegistryGenerator(5, 7, cityGenerator, personGenerator);
        lotteryGenerator = new LotteryGenerator(5);
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

    /**
    Try and set the fourth citizen in the list.
     */
    @Test
    public void testIndexNavigation_replaceListItem() {
        Person citizen = personGenerator.generate();
        CitizensRegistry registry = registryGenerator.generate(new FieldMod("personsList[3]", citizen));
        assertSame(citizen, registry.getPersonsList().get(3));
    }

    /**
     Try and set the name of the fourth citizen in the list.
     */
    @Test
    public void testIndexNavigation_setListItemField() {
        String firstName = faker.name().firstName();
        CitizensRegistry registry = registryGenerator.generate(new FieldMod("personsList[3].firstName", firstName));
        assertEquals(firstName, registry.getPersonsList().get(3).getFirstName());
    }

    @Test
    public void testIndexNavigation_setFieldOnAllListItems() {
        String firstName = faker.name().firstName();
        CitizensRegistry registry = registryGenerator.generate(new FieldMod("personsList[*].firstName", firstName));
        for (Person person : registry.getPersonsList()) {
            assertEquals(firstName, person.getFirstName());
        }
    }

    @Test
    public void testIndexNavigation_setFieldOnAllSetItems() {
        String firstName = faker.name().firstName();
        CitizensRegistry registry = registryGenerator.generate(new FieldMod("personsSet[*].firstName", firstName));
        for (Person person : registry.getPersonsSet()) {
            assertEquals(firstName, person.getFirstName());
        }
    }

    @Test
    public void testIndexNavigation_setFieldOnAllMapItems() {
        String firstName = faker.name().firstName();
        CitizensRegistry registry = registryGenerator.generate(new FieldMod("personsByFullName[*].firstName", firstName));
        for (Person person : registry.getPersonsByFullName().values()) {
            assertEquals(firstName, person.getFirstName());
        }
    }

    @Test
    public void testIndexNavigation_nestedIndexes() {
        int streetNumber = faker.random().nextInt(Integer.MAX_VALUE);
        CitizensRegistry registry = registryGenerator.generate(new FieldMod("personsList[*].residentAt[*].number", streetNumber));
        for (Person person : registry.getPersonsList())
            for (Address residence : person.getResidentAt())
                assertEquals(streetNumber, residence.getNumber());
    }

    @Test
    public void testIndexNavigation_primitiveArray() {
        byte setTo = (byte) -1;
        Lottery lottery = lotteryGenerator.generate(new FieldMod("winningDigits[*]", setTo));
        for (byte digit : lottery.getWinningDigits())
            assertEquals(setTo, digit);
    }
}