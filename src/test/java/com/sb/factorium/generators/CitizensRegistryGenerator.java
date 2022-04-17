package com.sb.factorium.generators;

import com.sb.factorium.FakerGenerator;
import com.sb.factorium.FieldMod;
import com.sb.factorium.Generator;
import com.sb.factorium.beans.CitizensRegistry;
import com.sb.factorium.beans.City;
import com.sb.factorium.beans.Person;

public class CitizensRegistryGenerator extends FakerGenerator<CitizensRegistry> {

    private int minCitizens;
    private int maxCitizens;
    private Generator<City> cityGenerator;
    private Generator<Person> personGenerator;

    public CitizensRegistryGenerator(int minCitizens, int maxCitizens, Generator<City> cityGenerator, Generator<Person> personGenerator) {
        this.minCitizens = minCitizens;
        this.maxCitizens = maxCitizens;
        this.cityGenerator = cityGenerator;
        this.personGenerator = personGenerator;
    }

    @Override
    protected CitizensRegistry make() {
        City city = cityGenerator.generate();
        CitizensRegistry registry = new CitizensRegistry(city);
        final int N_CITIZENS = faker.number().numberBetween(minCitizens, maxCitizens);
        FieldMod citySetter = new FieldMod("citizenOf", city);
        for (int i = 0; i < N_CITIZENS; i++) {
            registry.register(personGenerator.generate(citySetter));
        }
        return registry;
    }
}
