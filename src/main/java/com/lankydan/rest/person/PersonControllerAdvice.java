package com.lankydan.rest.person;

import com.lankydan.repository.membership.exception.GymMembershipNotFoundException;
import com.lankydan.respository.person.exception.PersonNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

@ControllerAdvice
@RequestMapping(produces = "application/vnd.error+json")
public class PersonControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler(PersonNotFoundException.class)
  public ResponseEntity<VndErrors> notFoundException(final PersonNotFoundException e) {
    return error(e, HttpStatus.NOT_FOUND, e.getId().toString());
  }

  @ExceptionHandler(PersonNotFoundException.class)
  public ResponseEntity<VndErrors> notFoundException2(final PersonNotFoundException e) {
    return error(e, HttpStatus.NOT_FOUND, e.getId().toString());
  }

  private ResponseEntity<VndErrors> error(
      final Exception exception, final HttpStatus httpStatus, final String logRef) {
    final String message =
        Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
    return new ResponseEntity<>(new VndErrors(logRef, message), httpStatus);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<VndErrors> assertionException(final IllegalArgumentException e) {
    return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
  }
}
