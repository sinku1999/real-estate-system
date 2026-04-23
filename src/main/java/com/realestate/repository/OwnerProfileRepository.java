package com.realestate.repository;

import com.realestate.entity.OwnerProfile;
import com.realestate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerProfileRepository extends JpaRepository<OwnerProfile, Long> {

    Optional<OwnerProfile> findByUser(User user);
}