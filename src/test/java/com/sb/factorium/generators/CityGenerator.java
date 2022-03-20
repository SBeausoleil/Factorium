package com.sb.factorium.generators;

import com.sb.factorium.FakerGenerator;
import com.sb.factorium.GeneratorInfo;
import com.sb.factorium.beans.City;

@GeneratorInfo(target = City.class, name = CityGenerator.KEY, isDefault = true)
public class CityGenerator extends FakerGenerator<City> {
    /**
     * Suggested key for the default city generator.
     */
    public static final String KEY = "default";

    @Override
    protected City make() {
        return new City(faker.name().lastName(), Math.abs(faker.random().nextLong()) + 1);
    }

    @GeneratorInfo(target = City.class, name = CapitalGenerator.KEY)
    public static class CapitalGenerator extends FakerGenerator<City> {
        /**
         * Suggested key for the capital city generator.
         */
        public static final String KEY = "capital";

        @Override
        protected City make() {
            return new City(faker.country().capital(), Math.abs(faker.random().nextLong()) + 1);
        }
    }
}
