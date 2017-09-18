package com.lankydan.rest.person;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lankydan.entity.person.Person;
import com.lankydan.respository.person.PersonRepository;
import com.lankydan.respository.person.exception.PersonNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(PersonController.class)
public class PersonControllerTest {

  //  @Autowired
  private MockMvc mockMvc;

  @Autowired private ObjectMapper mapper;

  @MockBean private PersonRepository personRepository;

  private static final DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

  /*
  localhost path added (originally was /people) because the href returned contains localhost in its string
   */
  private static String BASE_PATH = "http://localhost/people";
  private static String MEMBERSHIPS_PATH = "/memberships";
  private static final long ID = 1;
  private Person person;

  @Before
  public void setup() {
    mockMvc =
        MockMvcBuilders.standaloneSetup(new PersonController(personRepository))
            .setControllerAdvice(new PersonControllerAdvice())
            .build();
    setupPerson();
  }

  private void setupPerson() {
    person = new Person();
    person.setId(ID);
    person.setFirstName("first");
    person.setSecondName("second");
    person.setDateOfBirth(LocalDateTime.now());
    person.setProfession("developer");
    person.setSalary(0);
  }

  @Test
  public void getReturnsCorrectResponse() throws Exception {
    given(personRepository.findById(ID)).willReturn(Optional.of(person));
    final ResultActions result = mockMvc.perform(get(BASE_PATH + "/" + ID));
    result.andExpect(status().isOk());
    verifyJson(result);
  }

  /*
  private void verifyJson(final ResultActions action) throws Exception {
    action
        .andExpect(jsonPath("person.id", is(person.getId().intValue())))
        .andExpect(jsonPath("person.firstName", is(person.getFirstName())))
        .andExpect(jsonPath("person.secondName", is(person.getSecondName())))
        .andExpect(jsonPath("person.dateOfBirth", is(person.getDateOfBirth().format(formatter))))
        .andExpect(jsonPath("person.profession", is(person.getProfession())))
        .andExpect(jsonPath("person.salary", is(person.getSalary())))
        .andExpect(jsonPath("_links.people.href", is(BASE_PATH)))
        .andExpect(jsonPath("_links.memberships.href", is(BASE_PATH + "/" + ID + MEMBERSHIPS_PATH)))
        .andExpect(jsonPath("_links.self.href", is(BASE_PATH + "/" + ID)));
  }
  */

  private void verifyJson(final ResultActions action) throws Exception {
    action
        .andExpect(jsonPath("person.id", is(person.getId().intValue())))
        .andExpect(jsonPath("person.firstName", is(person.getFirstName())))
        .andExpect(jsonPath("person.secondName", is(person.getSecondName())))
        .andExpect(jsonPath("person.dateOfBirth", is(person.getDateOfBirth().format(formatter))))
        .andExpect(jsonPath("person.profession", is(person.getProfession())))
        .andExpect(jsonPath("person.salary", is(person.getSalary())))
        .andExpect(jsonPath("links[0].rel", is("people")))
        .andExpect(jsonPath("links[0].href", is(BASE_PATH)))
        .andExpect(jsonPath("links[1].rel", is("memberships")))
        .andExpect(jsonPath("links[1].href", is(BASE_PATH + "/" + ID + MEMBERSHIPS_PATH)))
        .andExpect(jsonPath("links[2].rel", is("self")))
        .andExpect(jsonPath("links[2].href", is(BASE_PATH + "/" + ID)));
  }

  @Test
  public void allReturnsCorrectResponse() throws Exception {
    given(personRepository.findAll()).willReturn(Arrays.asList(person));
    final ResultActions result = mockMvc.perform(get(BASE_PATH));
    result.andExpect(status().isOk());
    result
        .andExpect(jsonPath("links[0].rel", is("self")))
        .andExpect(jsonPath("links[0].href", is(BASE_PATH)))
        .andExpect(jsonPath("content[0].person.id", is(person.getId().intValue())))
        .andExpect(jsonPath("content[0].person.id", is(person.getId().intValue())))
        .andExpect(jsonPath("content[0].person.firstName", is(person.getFirstName())))
        .andExpect(jsonPath("content[0].person.secondName", is(person.getSecondName())))
        .andExpect(
            jsonPath(
                "content[0].person.dateOfBirth", is(person.getDateOfBirth().format(formatter))))
        .andExpect(jsonPath("content[0].person.profession", is(person.getProfession())))
        .andExpect(jsonPath("content[0].person.salary", is(person.getSalary())))
        .andExpect(jsonPath("content[0].links[0].rel", is("people")))
        .andExpect(jsonPath("content[0].links[0].href", is(BASE_PATH)))
        .andExpect(jsonPath("content[0].links[1].rel", is("memberships")))
        .andExpect(
            jsonPath("content[0].links[1].href", is(BASE_PATH + "/" + ID + MEMBERSHIPS_PATH)))
        .andExpect(jsonPath("content[0].links[2].rel", is("self")))
        .andExpect(jsonPath("content[0].links[2].href", is(BASE_PATH + "/" + ID)));
  }

  @Test
  public void postReturnsCorrectResponse() throws Exception {
    given(personRepository.save(any(Person.class))).willReturn(person);
    final ResultActions result =
        mockMvc.perform(
            post(BASE_PATH)
                .content(mapper.writeValueAsBytes(person))
                .contentType(MediaType.APPLICATION_JSON_UTF8));
    result.andExpect(status().isCreated());
    verifyJson(result);
  }

  @Test
  public void putReturnsCorrectResponse() throws Exception {
    given(personRepository.save(any(Person.class))).willReturn(person);
    final ResultActions result =
        mockMvc.perform(
            put(BASE_PATH + "/" + ID)
                .content(mapper.writeValueAsBytes(person))
                .contentType(MediaType.APPLICATION_JSON_UTF8));
    result.andExpect(status().isCreated());
    verifyJson(result);
  }

  @Test
  public void deleteReturnsCorrectResponse() throws Exception {
    given(personRepository.findById(ID)).willReturn(Optional.of(person));
    mockMvc
        .perform(delete(BASE_PATH + "/" + ID))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));
  }

  // Equivalent tests for PUT and DELETE could be made to test handling of same error
  @Test
  public void getPersonThatDoesNotExistReturnsError() throws Exception {
    final PersonNotFoundException exception = new PersonNotFoundException(ID);
    given(personRepository.findById(ID)).willReturn(Optional.empty());
    final ResultActions result = mockMvc.perform(get(BASE_PATH + "/" + ID));
    result.andExpect(status().isNotFound());
    result
        .andExpect(jsonPath("$[0].logref", is(String.valueOf(ID))))
        .andExpect(jsonPath("$[0].message", is(exception.getMessage())))
        .andExpect(jsonPath("$[0].links", is(new ArrayList<String>())));
  }
}
