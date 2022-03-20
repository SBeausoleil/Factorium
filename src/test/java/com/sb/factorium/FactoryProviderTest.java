package com.sb.factorium;

import com.sb.factorium.beans.Address;
import com.sb.factorium.beans.City;
import com.sb.factorium.generators.AddressGenerator;
import com.sb.factorium.generators.CityGenerator;
import com.sb.factorium.generators.PersonGenerator;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;


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
        generators.add(new CityGenerator.CapitalGenerator());
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
        assertSame(addressGenerator.getCityGenerator(), cityGenerator);
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

    @Test
    public void testComputeDefaultKey_defaultKey() {
        assertEquals(FactoryProvider.DEFAULT_KEY, FactoryProvider.computeDefaultKey(FactoryProvider.DefaultKey.DEFAULT_KEY, cityGenerator));
    }

    @Test
    public void testComputeDefaultKey_firstGenerator() {
        assertEquals(CityGenerator.CapitalGenerator.KEY,
                FactoryProvider.computeDefaultKey(FactoryProvider.DefaultKey.FIRST_GENERATOR, new CityGenerator.CapitalGenerator()));
    }

    private static class GeneratorContainingObject extends BaseGenerator<Object> {
        static final String REF_VALUE = "Hello World";
        Object objectReference = REF_VALUE;

        @Override
        protected Object make() {
            return new Object();
        }
    }

    /**
     * Test that a generator that holds an Object field does not get that field replaced.
     * @throws IllegalAccessException
     */
    public void testCreateRecordingFactoryProvider_generatorWithObject() throws IllegalAccessException {
        GeneratorContainingObject generatorContainingObject = new GeneratorContainingObject();

        generators.add(generatorContainingObject);
        FactoryProvider<RecordingFactory<String, ?>> provider = FactoryProvider.make(
                generators,
                FactoryProvider.DefaultKey.DEFAULT_KEY,
                new RecordingFactoryMaker(),
                true);

        assertNotNull(provider);
        assertEquals(GeneratorContainingObject.REF_VALUE, generatorContainingObject.objectReference);
    }
}