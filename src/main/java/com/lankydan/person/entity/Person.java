package com.lankydan.person.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"id", "dateOfBirth"})
@Entity
@Builder
@AllArgsConstructor
public class Person {

  @Id
  private String id;
  private String firstName;
  private String secondName;
  @JsonFormat(pattern = "dd/MM/yyyy")
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private LocalDateTime dateOfBirth;
  private String profession;
  private int salary;
  private List<Hobby> hobbies;

//  public Person(
//      final String firstName,
//      final String secondName,
//      final LocalDateTime dateOfBirth,
//      final String profession,
//      final int salary) {
//    this.firstName = firstName;
//    this.secondName = secondName;
//    this.dateOfBirth = dateOfBirth;
//    this.profession = profession;
//    this.salary = salary;
//  }

}
