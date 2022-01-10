package com.sb.factorium.generators;

import com.sb.factorium.FakerGenerator;
import com.sb.factorium.beans.Address;

public class AddressGenerator extends FakerGenerator<Address> {
    @Override
    protected Address make() {
        return new Address(
                faker.number().randomDigitNotZero(),
                faker.address().streetName(),
                faker.address().zipCode(),
                new CityGenerator().generate()
        );
    }
}
