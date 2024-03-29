package ru.job4j.rest.service;

import ru.job4j.rest.domain.Person;
import ru.job4j.rest.dto.PersonPasswordDTO;

import java.util.Collection;
import java.util.Optional;

public interface PersonService {

    Optional<Person> save(Person person);

    boolean update(Person person);

    Optional<Person> updatePassword(PersonPasswordDTO person);

    boolean deleteById(int id);

    Optional<Person> findById(int id);

    Collection<Person> findAll();

}
