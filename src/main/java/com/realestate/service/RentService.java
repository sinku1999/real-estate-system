package com.realestate.service;

import com.realestate.entity.RentBooking;
import java.util.List;

public interface RentService {

    void createBooking(Long propertyId, String customerEmail);

    void approve(Long bookingId);

    void pay(Long bookingId, String customerEmail);

    List<RentBooking> getAllBookings();   // ✅ NEW
}