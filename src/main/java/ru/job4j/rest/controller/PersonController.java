package ru.job4j.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.rest.domain.Person;
import ru.job4j.rest.service.PersonService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/person")
public class PersonController {

    private final PersonService personService;

    @GetMapping("")
    public ResponseEntity<List<Person>> findAll() {
        return ResponseEntity.ok((List<Person>) personService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> findById(@PathVariable int id) {
        var personOptional = personService.findById(id);
        return personOptional.map(ResponseEntity::ok).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Index is not found"));
    }

    @PostMapping("")
    public ResponseEntity<Person> create(@RequestBody Person person) {
        checkPerson(person);
        return personService.save(person)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PutMapping("")
    public ResponseEntity<Void> update(@RequestBody Person person) {
        checkPerson(person);
        if (personService.update(person)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        if (personService.deleteById(id)) {
            return ResponseEntity.ok().build();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Index is not found");
    }

    @GetMapping("/example1")
    public ResponseEntity<byte[]> response1() throws IOException {
        return ResponseEntity.status(201)
                .header("header1", "headerValues")
                .contentType(MediaType.APPLICATION_PDF)
                .body(Files.readAllBytes(Path.of("d://test.pdf")));
    }

    @GetMapping("/example2")
    public ResponseEntity<?> response2() {
        return new ResponseEntity<>(
                "body",
                new MultiValueMapAdapter<>(Map.of("key1", List.of("listOf"))),
                201
        );
    }

    private void checkPerson(Person person) {
        if (person.getLogin().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("The field is empty");
        }
    }

}