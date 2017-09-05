package com.lankydan.person.rest;

import com.lankydan.person.repository.exception.PersonNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
See its own comments. It works based on packages / annotations
(hence why example I used was annotations = RestController.class).
So any method that is marked with @ExceptionHandler / @ModelAttribute / @InitBinder
will use the exception handlers created in this class.
 */
@ControllerAdvice("com.lankydan.rest")
public class PersonControllerAdvice {

  // below is suggested by spring REST guide
  //  @ResponseBody
  //  @ExceptionHandler(UserNotFoundException.class)
  //  @ResponseStatus(HttpStatus.NOT_FOUND)
  //  VndErrors userNotFoundExceptionHandler(UserNotFoundException ex) {
  //    return new VndErrors("error", ex.getMessage());
  //  }

  // is the below correct? based on starbuxmans cloud native java book (which is new)

  private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error");

  @ExceptionHandler(PersonNotFoundException.class)
  public ResponseEntity<VndErrors> notFoundException(PersonNotFoundException e) {
    return error(e, HttpStatus.NOT_FOUND, e.getId());
  }

  private <E extends Exception> ResponseEntity<VndErrors> error(
      final E exception, final HttpStatus httpStatus, final String logRef) {
    final String message =
        Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(vndErrorMediaType);
    return new ResponseEntity<>(new VndErrors(logRef, message), httpHeaders, httpStatus);
  }

  // correct http status?
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<VndErrors> assertionException(IllegalArgumentException e) {
    return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
  }
}
