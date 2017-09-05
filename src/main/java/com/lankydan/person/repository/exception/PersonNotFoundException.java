package com.lankydan.person.repository.exception;

import lombok.Getter;

@Getter
public class PersonNotFoundException extends RuntimeException {

  private final String id;

  public PersonNotFoundException(final String id) {
    super("Person could not be found with id: " + id);
    this.id = id;
  }

}
