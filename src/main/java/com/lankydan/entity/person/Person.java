package com.lankydan.entity.person;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lankydan.entity.membership.GymMembership;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "people")
@NoArgsConstructor
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String firstName;

  private String secondName;

  @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
  @DateTimeFormat(pattern = "dd/MM/yyyy")
  private LocalDateTime dateOfBirth;

  private String profession;

  private int salary;

  @JsonIgnore
  @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
  private List<GymMembership> memberships;

  public Person(final Person person) {
    this.firstName = person.getFirstName();
    this.secondName = person.getSecondName();
    this.dateOfBirth = person.getDateOfBirth();
    this.profession = person.getProfession();
    this.salary = person.getSalary();
    this.memberships = person.getMemberships();
  }

  public Person(final Person person, final long id) {
    this.id = id;
    this.firstName = person.getFirstName();
    this.secondName = person.getSecondName();
    this.dateOfBirth = person.getDateOfBirth();
    this.profession = person.getProfession();
    this.salary = person.getSalary();
    this.memberships = person.getMemberships();
  }

}
