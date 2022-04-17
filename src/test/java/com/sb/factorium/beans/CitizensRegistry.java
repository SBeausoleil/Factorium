package com.sb.factorium.beans;

import com.sb.factorium.beans.City;
import com.sb.factorium.beans.Person;

import java.util.*;

public class CitizensRegistry {
    private City city;
    private List<Person> personsList;
    private Set<Person> personsSet;
    private Map<String, Person> personsByFullName;

    public CitizensRegistry(City city) {
        this.city = city;
        personsList = new ArrayList<>();
        personsSet = new HashSet<>();
        personsByFullName = new HashMap<>();
    }

    public void register(Person citizen) {
        personsList.add(citizen);
        personsSet.add(citizen);
        personsByFullName.put(citizen.getFullName(), citizen);
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public List<Person> getPersonsList() {
        return personsList;
    }

    public void setPersonsList(List<Person> personsList) {
        this.personsList = personsList;
    }

    public Set<Person> getPersonsSet() {
        return personsSet;
    }

    public void setPersonsSet(Set<Person> personsSet) {
        this.personsSet = personsSet;
    }

    public Map<String, Person> getPersonsByFullName() {
        return personsByFullName;
    }

    public void setPersonsByFullName(Map<String, Person> personsByFullName) {
        this.personsByFullName = personsByFullName;
    }
}
