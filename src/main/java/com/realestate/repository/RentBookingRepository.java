package com.realestate.repository;

import com.realestate.entity.RentBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentBookingRepository extends JpaRepository<RentBooking, Long> {
}