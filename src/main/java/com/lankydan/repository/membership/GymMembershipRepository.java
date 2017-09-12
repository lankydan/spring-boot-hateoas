package com.lankydan.repository.membership;

import com.lankydan.entity.membership.GymMembership;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GymMembershipRepository extends JpaRepository<GymMembership, Long> {}
