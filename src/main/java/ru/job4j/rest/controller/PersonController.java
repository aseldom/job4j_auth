package ru.job4j.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.rest.domain.Person;
import ru.job4j.rest.service.PersonService;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;
    private BCryptPasswordEncoder encoder;

    @GetMapping("/")
    public List<Person> findAll() {
        return (List<Person>) personService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var person = personService.findById(id);
        return new ResponseEntity<>(
                person.orElse(new Person()),
                person.isPresent() ? HttpStatus.OK : HttpStatus.NOT_FOUND
        );
    }

    @PostMapping("/")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        Optional<Person> personOptional = personService.save(person);
        return new ResponseEntity<>(
                personOptional.orElse(new Person()),
                personOptional.isPresent() ? HttpStatus.OK : HttpStatus.CONFLICT
        );
    }

    @PutMapping("/")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        return  personService.update(person)
                ? ResponseEntity.ok().build()
                : ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        return personService.deleteById(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();

    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person person) {
        person.setPassword(encoder.encode(person.getPassword()));
        personService.save(person);
    }
}