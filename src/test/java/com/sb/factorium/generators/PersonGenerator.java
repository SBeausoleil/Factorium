package com.sb.factorium.generators;

import com.sb.factorium.FakerGenerator;
import com.sb.factorium.FieldMod;
import com.sb.factorium.Generator;
import com.sb.factorium.beans.Address;
import com.sb.factorium.beans.City;
import com.sb.factorium.beans.Person;

import java.util.List;

public class PersonGenerator extends FakerGenerator<Person> {
    public static final String KEY = "default";

    private Generator<City> cityGenerator;
    private Generator<Address> addressGenerator;

    public PersonGenerator(Generator<City> cityGenerator, Generator<Address> addressGenerator) {
        this.cityGenerator = cityGenerator;
        this.addressGenerator = addressGenerator;
    }

    @Override
    protected Person make() {
        City citizenOf = cityGenerator.generate();
        List<Address> addresses = addressGenerator.generate(faker.number().numberBetween(0, 2));
        Address mainResidency = addressGenerator.generate(new FieldMod("city", citizenOf));
        addresses.add(mainResidency);

        return new Person(
                faker.name().firstName(),
                faker.name().lastName(),
                citizenOf,
                addresses
        );
    }
}
