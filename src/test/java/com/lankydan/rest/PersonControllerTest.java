package com.lankydan.rest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.core.Is.is;

import com.lankydan.entity.person.Person;
import com.lankydan.respository.person.PersonRepository;
import com.lankydan.rest.person.PersonController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private PersonRepository personRepository;

  private static String BASE_PATH = "/people";
  private static final long ID = 1;

  //  private static final String GET_JSON =
  //      "{  \"person\": {    \"id\": 1,    \"firstName\": \"test\",    \"secondName\": \"one\",
  // \"dateOfBirth\": \"01/01/0001 01:10\",    \"profession\": \"im a test\",    \"salary\": 0  },
  // \"_links\": {    \"people\": {      \"href\": \"http://localhost:8090/people\"    },
  // \"memberships\": {      \"href\": \"http://localhost:8090/people/1/memberships\"    },
  // \"self\": {      \"href\": \"http://localhost:8090/people/1\"    }  }}";

  @Test
  public void getReturnsCorrectJson() throws Exception {
    final Person person = validPerson();
    given(personRepository.findById(ID)).willReturn(Optional.of(person));
    mockMvc
        .perform(get(BASE_PATH + "/" + ID))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id", is(person.getId())));
  }

  private Person validPerson() {
    final Person person = new Person();
    person.setId(ID);
    person.setFirstName("first");
    person.setSecondName("second");
    person.setDateOfBirth(LocalDateTime.now());
    person.setProfession("developer");
    person.setSalary(0);
    return person;
  }
}
