package com.sb.factorium.generators;

import com.sb.factorium.FakerGenerator;
import com.sb.factorium.beans.City;

public class CityGenerator extends FakerGenerator<City> {
    /**
     * Suggested key for the default city generator.
     */
    public static String KEY = "default";

    @Override
    protected City make() {
        return new City(faker.name().lastName(), Math.abs(faker.random().nextLong()) + 1);
    }

    public static class CapitalGenerator extends FakerGenerator<City> {
        /**
         * Suggested key for the capital city generator.
         */
        public static String KEY = "capital";

        @Override
        protected City make() {
            return new City(faker.country().capital(), Math.abs(faker.random().nextLong()) + 1);
        }
    }
}
