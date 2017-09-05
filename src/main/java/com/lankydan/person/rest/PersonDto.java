package com.lankydan.person.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class PersonDto {

  // Do I actually need to use a DTO. I am currently returning the Person entity directly, or
  // should a dto be returned instead?

  private String firstName;
  private String secondName;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
//  @JsonFormat(pattern = "yyyy-MM-dd")
//  @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
  private LocalDateTime dateOfBirth;

  private String profession;
  private int salary;
}
