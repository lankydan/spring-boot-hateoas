package com.lankydan.person.rest;

import com.lankydan.person.entity.Person;
import com.lankydan.person.repository.PersonRepository;
import com.lankydan.person.repository.exception.PersonNotFoundException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/people", produces = "application/hal+json")
public class PersonController {

  /*
  Need to research into HATEOAS (Hypermedia as the engine of application state)
  which is the reason for writing the REST API as such

  Need to know why it should be done
  Returning a resource rather than the entity/entity/object/dto which raps the object
  Resource has URI's in it? For information about the REST API method that is being used?
   */

  final PersonRepository personRepository;

  public PersonController(final PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @GetMapping
  public ResponseEntity<Resources<PersonResource>> all() {
    final List<PersonResource> collection =
        personRepository.findAll().stream().map(PersonResource::new).collect(Collectors.toList());
    final Resources<PersonResource> resources = new Resources<>(collection);
    final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
    resources.add(new Link(uriString, "self"));
    return ResponseEntity.ok(resources);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PersonResource> get(@PathVariable final String id) {
    return personRepository
        .findById(id)
        .map(p -> ResponseEntity.ok(new PersonResource(p)))
        .orElseThrow(() -> new PersonNotFoundException(id));
  }

  @PostMapping
  public ResponseEntity<?> post(@RequestBody final PersonDto personDto) {
    // add a dto later on for passing in
    final Person person =
        Person.builder()
            .firstName(personDto.getFirstName())
            .secondName(personDto.getSecondName())
            .dateOfBirth(personDto.getDateOfBirth())
            .profession(personDto.getProfession())
            .salary(personDto.getSalary())
            .build();
    personRepository.save(person);
    final URI uri =
        MvcUriComponentsBuilder.fromController(getClass())
            .path("/{id}")
            .buildAndExpand(person.getId())
            .toUri();
    return ResponseEntity.created(uri).body(new PersonResource(person));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PersonResource> put(
      @PathVariable("id") final String id, @RequestBody PersonDto personDto) {
    // id would be used to create a new entity with the id
    // so when it is saved it will update the existing record
    final Person person =
        Person.builder()
            .id(id)
            .firstName(personDto.getFirstName())
            .secondName(personDto.getSecondName())
            .dateOfBirth(personDto.getDateOfBirth())
            .profession(personDto.getProfession())
            .salary(personDto.getSalary())
            .build();
    personRepository.save(person);
    final PersonResource resource = new PersonResource(person);
    // uri returned is the uri to the updated resource (like in rails you update a user it then displays the user info on the next page)
    final URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
    return ResponseEntity.created(uri).body(resource);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable("id") final String id) {
    return personRepository
        .findById(id)
        .map(
            p -> {
              personRepository.deleteById(id);
              return ResponseEntity.noContent().build();
            })
        .orElseThrow(() -> new PersonNotFoundException(id));
  }
}
