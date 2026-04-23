package com.realestate.repository;

import com.realestate.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    // ✅ REQUIRED for PG rooms page
    List<Room> findByPropertyId(Long propertyId);
}