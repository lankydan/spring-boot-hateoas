package com.lankydan.rest.membership;

import com.lankydan.entity.membership.GymMembership;
import com.lankydan.rest.person.PersonController;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class GymMembershipResource extends ResourceSupport {

  private final GymMembership gymMembership;

  public GymMembershipResource(final GymMembership gymMembership) {
    this.gymMembership = gymMembership;
    final long membershipId = gymMembership.getId();
    final long personId = gymMembership.getOwner().getId();
    add(new Link(String.valueOf(membershipId), "membership-id"));
    add(linkTo(methodOn(GymMembershipController.class).all(personId)).withRel("memberships"));
    add(linkTo(methodOn(PersonController.class).get(personId)).withRel("owner"));
    add(linkTo(methodOn(GymMembershipController.class).get(personId, membershipId)).withSelfRel());
  }
}
