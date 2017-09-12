package com.lankydan.rest.membership;

import com.lankydan.entity.membership.GymMembership;
import com.lankydan.repository.membership.GymMembershipRepository;
import com.lankydan.repository.membership.exception.GymMembershipNotFoundException;
import com.lankydan.entity.person.Person;
import com.lankydan.respository.person.PersonRepository;
import com.lankydan.respository.person.exception.PersonNotFoundException;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people/{personId}/memberships")
public class GymMembershipController {

  private PersonRepository personRepository;

  private GymMembershipRepository gymMembershipRepository;

  public GymMembershipController(
      final PersonRepository personRepository,
      final GymMembershipRepository gymMembershipRepository) {
    this.personRepository = personRepository;
    this.gymMembershipRepository = gymMembershipRepository;
  }

  @GetMapping
  public ResponseEntity<Resources<GymMembershipResource>> all(@PathVariable final long personId) {
    final List<GymMembershipResource> collection = getMembershipsForPerson(personId);
    final Resources<GymMembershipResource> resources = new Resources<>(collection);
    final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
    resources.add(new Link(uriString, "self"));
    return ResponseEntity.ok(resources);
  }

  private List<GymMembershipResource> getMembershipsForPerson(final long personId) {
    return personRepository
        .findById(personId)
        .map(
            p ->
                p.getMemberships()
                    .stream()
                    .map(GymMembershipResource::new)
                    .collect(Collectors.toList()))
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }

  private void validatePerson(final long personId) {
    personRepository.findById(personId).orElseThrow(() -> new PersonNotFoundException(personId));
  }

  @GetMapping("/{membershipId}")
  public ResponseEntity<GymMembershipResource> get(
      @PathVariable final long personId, @PathVariable final long membershipId) {
    return personRepository
        .findById(personId)
        .map(
            p ->
                p.getMemberships()
                    .stream()
                    .filter(m -> m.getId().equals(membershipId))
                    .findAny()
                    .map(m -> ResponseEntity.ok(new GymMembershipResource(m)))
                    .orElseThrow(() -> new GymMembershipNotFoundException(membershipId)))
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }

  @PostMapping
  public ResponseEntity<GymMembershipResource> post(
      @PathVariable final long personId, @RequestBody final GymMembership inputMembership) {
    return personRepository
        .findById(personId)
        .map(
            p -> {
              final GymMembership membership = saveMembership(p, inputMembership);
              final URI uri = createPostUri(membership);
              return ResponseEntity.created(uri).body(new GymMembershipResource(membership));
            })
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }

  private GymMembership saveMembership(final Person person, final GymMembership membership) {
    return gymMembershipRepository.save(
        new GymMembership(person, membership.getName(), membership.getCost()));
  }

  private URI createPostUri(final GymMembership membership) {
    return MvcUriComponentsBuilder.fromController(getClass())
        .path("/{membershipId}")
        .buildAndExpand(membership.getOwner().getId(), membership.getId())
        .toUri();
  }

  @PutMapping("/{membershipId}")
  public ResponseEntity<GymMembershipResource> put(
      @PathVariable final long personId,
      @PathVariable final long membershipId,
      @RequestBody final GymMembership inputMembership) {
    return personRepository
        .findById(personId)
        .map(
            p -> {
              final GymMembership membership = updateMembership(p, membershipId, inputMembership);
              final URI uri =
                  URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
              return ResponseEntity.created(uri).body(new GymMembershipResource(membership));
            })
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }

  private GymMembership updateMembership(
      final Person person, final long id, final GymMembership membership) {
    return gymMembershipRepository.save(
        new GymMembership(id, person, membership.getName(), membership.getCost()));
  }

  @DeleteMapping("/{membershipId}")
  public ResponseEntity<?> delete(
      @PathVariable final long personId, @PathVariable final long membershipId) {
    return personRepository
        .findById(personId)
        .map(
            p ->
                p.getMemberships()
                    .stream()
                    .filter(m -> m.getId().equals(membershipId))
                    .findAny()
                    .map(
                        m -> {
                          gymMembershipRepository.delete(m);
                          return ResponseEntity.noContent().build();
                        })
                    .orElseThrow(() -> new GymMembershipNotFoundException(membershipId)))
        .orElseThrow(() -> new PersonNotFoundException(personId));
  }
}
