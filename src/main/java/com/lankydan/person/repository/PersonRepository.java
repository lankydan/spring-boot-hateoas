package com.lankydan.person.repository;

import com.lankydan.person.entity.Person;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PersonRepository extends MongoRepository<Person, String> {}
