package com.sb.factorium.beans;

import java.util.Objects;

public class City {
    private String name;
    private long nCitizens;

    public City(String name, long nCitizens) {
        this.name = name;
        this.nCitizens = nCitizens;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getnCitizens() {
        return nCitizens;
    }

    public void setnCitizens(long nCitizens) {
        this.nCitizens = nCitizens;
    }

    private void privateSetterForCitizens(long nCitizens) {
        this.nCitizens = nCitizens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return nCitizens == city.nCitizens && Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, nCitizens);
    }
}
