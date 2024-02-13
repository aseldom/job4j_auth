package ru.job4j.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.job4j.rest.domain.Person;
import ru.job4j.rest.dto.PersonDTO;
import ru.job4j.rest.service.PersonService;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @PatchMapping("")
    public ResponseEntity<Person> update(@RequestBody PersonDTO personDTO) throws InvocationTargetException, IllegalAccessException {
        Optional<Person> personOptional = personService.findById(personDTO.getId());
        if (personOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Id is not correct");
        }
        Person person = personOptional.get();
        Map<String, Method> methodsPersonDTO = getMethods(personDTO);
        Map<String, Method> methodsPerson = getMethods(person);
        for (String methodName : methodsPerson.keySet()) {
            if (methodName.startsWith("get") && methodsPersonDTO.containsKey(methodName)) {
                Object newValue = methodsPersonDTO.get(methodName).invoke(personDTO);
                if (newValue != null) {
                    methodsPerson.get(methodName.replace("get", "set")).invoke(person, newValue);
                }
            }
        }
        personService.update(person);
        return ResponseEntity.ok().body(person);
    }

    private Map<String, Method> getMethods(Object object) {
        return Arrays.stream(object.getClass().getDeclaredMethods())
                .filter(a -> a.getName().startsWith("get") || a.getName().startsWith("set"))
                .collect(Collectors.toMap(Method::getName, a -> a));
    }

    private void checkPerson(Person person) {
        if (person.getLogin().isEmpty() || person.getPassword().isEmpty()) {
            throw new NullPointerException("The field is empty");
        }
    }

}