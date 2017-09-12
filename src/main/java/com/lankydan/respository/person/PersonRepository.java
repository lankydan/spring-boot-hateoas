package com.lankydan.respository.person;

import com.lankydan.entity.person.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}
