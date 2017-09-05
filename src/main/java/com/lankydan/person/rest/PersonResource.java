package com.lankydan.person.rest;

import com.lankydan.person.entity.Person;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/*
What is the point of a resource and ResourceSupport?
 */
public class PersonResource extends ResourceSupport {

  private final Person person;

  public PersonResource(final Person person) {
    this.person = person;
    final String id = person.getId();
    add(new Link(id, "person-id"));
    add(linkTo(PersonController.class).withRel("people"));
    add(linkTo(methodOn(PersonController.class).get(id)).withSelfRel());
  }

  public Person getPerson() {
    return person;
  }
}
