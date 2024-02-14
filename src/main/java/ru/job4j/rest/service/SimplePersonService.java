package ru.job4j.rest.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.rest.domain.Person;
import ru.job4j.rest.dto.PersonPasswordDTO;
import ru.job4j.rest.repository.PersonRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimplePersonService implements PersonService {

    final static Logger LOGGER = LoggerFactory.getLogger(SimplePersonService.class);
    private BCryptPasswordEncoder encoder;
    private final PersonRepository personRepository;

    @Override
    public Optional<Person> save(Person person) {
        try {
            person.setPassword(encoder.encode(person.getPassword()));
            return Optional.of(personRepository.save(person));
        } catch (Exception e) {
            LOGGER.error("Error save person: " + e.getMessage());
        }
        return Optional.empty();
    }

    @Override
    public boolean update(Person person) {
        if (personRepository.existsById(person.getId())) {
            return save(person).isPresent();
        }
        return false;
    }

    @Override
    public Optional<Person> updatePassword(PersonPasswordDTO personPasswordDTO) {
        Optional<Person> personOptional = findById(personPasswordDTO.getId());
        if (personOptional.isEmpty()) {
            return Optional.empty();
        }
        Person newPerson = personOptional.get();
        newPerson.setPassword(personPasswordDTO.getPassword());
        return save(newPerson);
    }

    @Override
    public boolean deleteById(int id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    @Override
    public Collection<Person> findAll() {
        return personRepository.findAll();
    }

}
