package com.realestate.service;

import com.realestate.entity.PgBooking;
import com.realestate.entity.Property;
import com.realestate.entity.Room;   // ✅ ADD THIS

import java.util.List;

public interface PgService {

    void createBooking(Long roomId, String customerEmail);

    void approve(Long bookingId);

    void pay(Long bookingId, String customerEmail);

    List<PgBooking> getAllBookings();

    Property getProperty(Long propertyId);

    List<Room> getRoomsByProperty(Long propertyId);
}