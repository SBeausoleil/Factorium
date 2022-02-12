package com.sb.factorium.reflection;

import com.sb.factorium.BaseGenerator;
import com.sb.factorium.FakerGenerator;
import com.sb.factorium.Generator;
import com.sb.factorium.generators.AddressGenerator;
import com.sb.factorium.generators.CityGenerator;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.Assert.*;

public class ReflectionUtilTest {

    static class SpecificGenerator extends BaseGenerator<Object> {
        @Override
        protected Object make() {
            return new Object();
        }
    }

    static class GeneratorWithSpecificGenerator extends BaseGenerator<Object> {
        SpecificGenerator subgenerator;

        public GeneratorWithSpecificGenerator(SpecificGenerator subgenerator) {
            this.subgenerator = subgenerator;
        }

        @Override
        protected Object make() {
            return subgenerator.generate();
        }
    }

    static class GeneratorWithFinalSubgenerator extends BaseGenerator<Object> {
        final Generator<Object> subgenerator;

        public GeneratorWithFinalSubgenerator(Generator<Object> objectGenerator) {
            subgenerator = objectGenerator;
        }

        @Override
        protected Object make() {
            return subgenerator.generate();
        }
    }

    private SpecificGenerator specificGenerator;
    private GeneratorWithSpecificGenerator withSpecificSubgenerator;
    private GeneratorWithFinalSubgenerator withFinalSubgenerator;
    private CityGenerator cityGenerator;
    private AddressGenerator addressGenerator;

    @Before
    public void setUp() {
        specificGenerator = new SpecificGenerator();
        withSpecificSubgenerator = new GeneratorWithSpecificGenerator(specificGenerator);
        withFinalSubgenerator = new GeneratorWithFinalSubgenerator(specificGenerator);
        cityGenerator = new CityGenerator();
        addressGenerator = new AddressGenerator(cityGenerator);
    }

    @Test
    public void keepAssignableFrom_regularSubGenerator() throws NoSuchFieldException {

        Map<Class<?>, Field[]> fields = ReflectionUtil.getAllFields(addressGenerator.getClass());
        Map<Class<?>, Field[]> innerGenerators = ReflectionUtil.keepAssignableFrom(fields, Generator.class, false);

        // Check that a Generator is kept
        Field[] addressGeneratorFields = innerGenerators.get(AddressGenerator.class);
        Field cityGeneratorField = AddressGenerator.class.getDeclaredField("cityGenerator");
        assertTrue(ArrayUtils.contains(addressGeneratorFields, cityGeneratorField));

        // Check that a non-generator is not kept
        Field[] fakerGeneratorFields = innerGenerators.get(FakerGenerator.class);
        Field fakerField = FakerGenerator.class.getDeclaredField("faker");
        assertFalse(ArrayUtils.contains(fakerGeneratorFields, fakerField));
    }

    @Test
    public void keepAssignableFrom_doNotKeepFinalGenerators() throws NoSuchFieldException {
        Map<Class<?>, Field[]> fields = ReflectionUtil.getAllFields(withFinalSubgenerator.getClass());
        Map<Class<?>, Field[]> innerGenerators = ReflectionUtil.keepAssignableFrom(fields, Generator.class, false);

        Field finalField = GeneratorWithFinalSubgenerator.class.getDeclaredField("subgenerator");
        Field[] nonFinalGenerators = innerGenerators.get(GeneratorWithFinalSubgenerator.class);
        assertFalse(ArrayUtils.contains(nonFinalGenerators, finalField));
    }

    @Test
    public void keepAssignableFrom_keepFinalGenerators() throws NoSuchFieldException {
        Map<Class<?>, Field[]> fields = ReflectionUtil.getAllFields(withFinalSubgenerator.getClass());
        Map<Class<?>, Field[]> innerGenerators = ReflectionUtil.keepAssignableFrom(fields, Generator.class, true);

        Field finalField = GeneratorWithFinalSubgenerator.class.getDeclaredField("subgenerator");
        Field[] nonFinalGenerators = innerGenerators.get(GeneratorWithFinalSubgenerator.class);
        assertTrue(ArrayUtils.contains(nonFinalGenerators, finalField));
    }

    @Test
    public void testKeepAssignableFrom_noSpecificGenerator() throws NoSuchFieldException {
        Map<Class<?>, Field[]> fields = ReflectionUtil.getAllFields(withSpecificSubgenerator.getClass());
        Map<Class<?>, Field[]> innerGenerators = ReflectionUtil.keepAssignableFrom(fields, Generator.class, false);

        Field specificGenerator = GeneratorWithSpecificGenerator.class.getDeclaredField("subgenerator");
        Field[] nonFinalGenerators = innerGenerators.get(GeneratorWithFinalSubgenerator.class);
        assertFalse(ArrayUtils.contains(nonFinalGenerators, specificGenerator));
    }
}