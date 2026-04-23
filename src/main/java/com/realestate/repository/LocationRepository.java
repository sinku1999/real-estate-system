package com.realestate.repository;

import com.realestate.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    boolean existsByNameIgnoreCase(String name);

    Optional<Location> findByNameIgnoreCase(String name);
}