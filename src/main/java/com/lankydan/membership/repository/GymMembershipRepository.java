package com.lankydan.membership.repository;

import com.lankydan.membership.entity.GymMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GymMembershipRepository extends JpaRepository<GymMembership, Long> {}
