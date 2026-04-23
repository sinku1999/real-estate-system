package com.realestate.repository;

import com.realestate.entity.PgBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PgBookingRepository extends JpaRepository<PgBooking, Long> {
}