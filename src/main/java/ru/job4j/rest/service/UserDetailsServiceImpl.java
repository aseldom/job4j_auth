package ru.job4j.rest.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.rest.domain.Person;
import ru.job4j.rest.repository.PersonRepository;

import java.util.Optional;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<Person> optionalPerson = personRepository.findByLogin(login);
        if (optionalPerson.isEmpty()) {
            throw new UsernameNotFoundException(login);
        }
        Person person = optionalPerson.get();
        return new User(person.getLogin(), person.getPassword(), emptyList());
    }
}