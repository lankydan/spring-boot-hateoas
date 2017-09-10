package com.lankydan.person.rest;

import com.lankydan.membership.rest.GymMembershipController;
import com.lankydan.person.entity.Person;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class PersonResource extends ResourceSupport {

  private final Person person;

  public PersonResource(final Person person) {
    this.person = person;
    final long id = person.getId();
    add(new Link(String.valueOf(id), "person-id"));
    add(linkTo(PersonController.class).withRel("people"));
    add(linkTo(methodOn(GymMembershipController.class).all(id)).withRel("memberships"));
    add(linkTo(methodOn(PersonController.class).get(id)).withSelfRel());
  }
}
