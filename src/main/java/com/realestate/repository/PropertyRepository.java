package com.realestate.repository;

import com.realestate.entity.Property;
import com.realestate.entity.User;
import com.realestate.enums.PropertyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {

    List<Property> findByNameContainingIgnoreCase(String name);

    List<Property> findByLocation_Name(String location);

    @Query("SELECT DISTINCT p.location.name FROM Property p WHERE p.location IS NOT NULL ORDER BY p.location.name")
    List<String> findDistinctLocations();

    List<Property> findByOwner(User owner);

    List<Property> findByOwnerAndStatus(User owner, PropertyStatus status);

    List<Property> findByStatus(PropertyStatus status);

    List<Property> findByStatusAndNameContainingIgnoreCase(PropertyStatus status, String name);

    long countByOwner(User owner);

    long countByStatus(PropertyStatus status);
}