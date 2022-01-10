package com.sb.factorium.beans;

import java.util.Objects;

public class Address {
    private int number;
    private String streetName;
    private String postalCode;
    private City city;

    public Address(int number, String streetName, String postalCode, City city) {
        this.number = number;
        this.streetName = streetName;
        this.postalCode = postalCode;
        this.city = city;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return number == address.number && Objects.equals(streetName, address.streetName) && Objects.equals(postalCode, address.postalCode) && Objects.equals(city, address.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, streetName, postalCode, city);
    }
}
