package com.sb.factorium.generators;

import com.sb.factorium.FakerGenerator;
import com.sb.factorium.Generator;
import com.sb.factorium.beans.Address;
import com.sb.factorium.beans.City;

public class AddressGenerator extends FakerGenerator<Address> {

    private Generator<City> cityGenerator;

    public AddressGenerator(Generator<City> cityGenerator) {
        this.cityGenerator = cityGenerator;
    }

    @Override
    protected Address make() {
        return new Address(
                faker.number().randomDigitNotZero(),
                faker.address().streetName(),
                faker.address().zipCode(),
                cityGenerator.generate()
        );
    }

    public Generator<City> getCityGenerator() {
        return cityGenerator;
    }
}
