package com.sb.factorium;

import com.sb.factorium.beans.Address;
import com.sb.factorium.beans.City;
import com.sb.factorium.generators.AddressGenerator;
import com.sb.factorium.generators.CityGenerator;
import com.sb.factorium.generators.PersonGenerator;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertNotEquals;

public class FactoryProviderTest extends TestCase {

    private CityGenerator cityGenerator;
    private AddressGenerator addressGenerator;
    private PersonGenerator personGenerator;

    private Collection<Generator<?>> generators;

    @Override
    public void setUp() throws Exception {
        cityGenerator = new CityGenerator();
        addressGenerator = new AddressGenerator(cityGenerator);
        personGenerator = new PersonGenerator(cityGenerator, addressGenerator);

        generators = new ArrayList<>();
        generators.add(cityGenerator);
        generators.add(addressGenerator);
        generators.add(personGenerator);
    }

    public void testCreateRecordingFactoryProvider() throws IllegalAccessException {
        FactoryProvider<RecordingFactory<String, ?>> provider = FactoryProvider.make(
                generators,
                FactoryProvider.DefaultKey.DEFAULT_KEY,
                new RecordingFactoryMaker(),
                false);

        assertNotNull(provider);
        assertNotNull(provider.factory(Address.class));
        RecordingFactory<String, Address> recordingAdressFactory = (RecordingFactory<String, Address>) provider.factory(Address.class);
        assertNotEquals(0, recordingAdressFactory.generators.size());
        Address address = provider.factory(Address.class).generate();
        assertNotNull(address);

        // Check that references were not changed
        assertTrue(addressGenerator.getCityGenerator() == cityGenerator);
    }

    public void testCreateRecordingFactoryProvider_replacingReferences() throws IllegalAccessException {
        FactoryProvider<RecordingFactory<String, ?>> provider = FactoryProvider.make(
                generators,
                FactoryProvider.DefaultKey.DEFAULT_KEY,
                new RecordingFactoryMaker(),
                true);

        assertNotNull(provider);
        assertNotNull(provider.factory(Address.class));
        assertFalse(addressGenerator.getCityGenerator() instanceof CityGenerator);

        provider.factory(Address.class).generate();
        assertEquals(1, ((RecordingFactory) provider.factory(City.class)).created.size());
    }
}