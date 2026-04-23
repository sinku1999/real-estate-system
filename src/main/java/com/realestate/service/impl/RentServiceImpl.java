package com.realestate.service.impl;

import com.realestate.entity.Property;
import com.realestate.entity.RentBooking;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.RentBookingRepository;
import com.realestate.service.RentService;
import com.realestate.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RentServiceImpl implements RentService {

    private final RentBookingRepository rentRepo;
    private final PropertyRepository propertyRepo;
    private final WalletService walletService;

    @Override
    public void createBooking(Long propertyId, String customerEmail) {
        Property property = propertyRepo.findById(propertyId).orElseThrow();

        RentBooking booking = new RentBooking();
        booking.setPropertyId(property.getId());
        booking.setCustomerEmail(customerEmail);
        booking.setOwnerEmail(property.getOwner().getEmail());
        booking.setStatus("PENDING");
        booking.setMonthlyRent(property.getRent());
        booking.setTotalMonths(1);

        rentRepo.save(booking);
    }

    @Override
    public void approve(Long bookingId) {
        RentBooking booking = rentRepo.findById(bookingId).orElseThrow();
        booking.setStatus("APPROVED");
        rentRepo.save(booking);
    }

    @Override
    public void pay(Long bookingId, String customerEmail) {
        RentBooking booking = rentRepo.findById(bookingId).orElseThrow();

        walletService.transfer(customerEmail, booking.getOwnerEmail(), booking.getMonthlyRent());
    }

    @Override
    public List<RentBooking> getAllBookings() {
        return rentRepo.findAll();
    }
}