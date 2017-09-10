package com.lankydan.membership.rest;

import com.lankydan.membership.repository.exception.GymMembershipNotFoundException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

@ControllerAdvice("com.lankydan.membership.rest")
public class GymMembershipControllerAdvice {
  private final MediaType vndErrorMediaType = MediaType.parseMediaType("application/vnd.error");

  @ExceptionHandler(GymMembershipNotFoundException.class)
  public ResponseEntity<VndErrors> notFoundException(GymMembershipNotFoundException e) {
    return error(e, HttpStatus.NOT_FOUND, String.valueOf(e.getId()));
  }

  private <E extends Exception> ResponseEntity<VndErrors> error(
      final E exception, final HttpStatus httpStatus, final String logRef) {
    final String message =
        Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
    final HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(vndErrorMediaType);
    return new ResponseEntity<>(new VndErrors(logRef, message), httpHeaders, httpStatus);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<VndErrors> assertionException(IllegalArgumentException e) {
    return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
  }
}
