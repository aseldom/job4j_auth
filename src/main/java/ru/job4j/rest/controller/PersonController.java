package ru.job4j.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.rest.domain.Person;
import ru.job4j.rest.dto.PersonPasswordDTO;
import ru.job4j.rest.service.PersonService;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<Person> create(@RequestBody @Valid Person person) {
        return personService.save(person)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @PutMapping("")
    public ResponseEntity<Void> update(@RequestBody @Valid Person person) {
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

    @PatchMapping("")
    public ResponseEntity<Person> changePassword(@RequestBody @Valid PersonPasswordDTO passwordDTO) {
        return personService.updatePassword(passwordDTO).
                map(p -> ResponseEntity.ok().body(p))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

}