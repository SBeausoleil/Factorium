package com.sb.factorium.beans;

import java.util.List;
import java.util.Objects;

public class Person {

    private String firstName;
    private String lastName;
    private City citizenOf;
    private List<Address> residentAt;

    public Person(String firstName, String lastName, City citizenOf, List<Address> residentAt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.citizenOf = citizenOf;
        this.residentAt = residentAt;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public City getCitizenOf() {
        return citizenOf;
    }

    public void setCitizenOf(City citizenOf) {
        this.citizenOf = citizenOf;
    }

    public List<Address> getResidentAt() {
        return residentAt;
    }

    public void setResidentAt(List<Address> residentAt) {
        this.residentAt = residentAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(citizenOf, person.citizenOf) && Objects.equals(residentAt, person.residentAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, citizenOf, residentAt);
    }
}
