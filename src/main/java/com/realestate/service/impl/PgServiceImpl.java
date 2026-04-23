package com.realestate.service.impl;

import com.realestate.entity.PgBooking;
import com.realestate.entity.Property;
import com.realestate.entity.Room;
import com.realestate.repository.PgBookingRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.RoomRepository;
import com.realestate.service.PgService;
import com.realestate.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PgServiceImpl implements PgService {

    private final RoomRepository roomRepo;
    private final PgBookingRepository pgBookingRepo;
    private final PropertyRepository propertyRepo;
    private final WalletService walletService;

    @Override
    public void createBooking(Long roomId, String customerEmail) {
        Room room = roomRepo.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        Property property = propertyRepo.findById(room.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        PgBooking booking = new PgBooking();
        booking.setRoomId(room.getId());
        booking.setCustomerEmail(customerEmail);
        booking.setOwnerEmail(property.getOwner().getEmail());
        booking.setStatus("PENDING");
        booking.setMonthlyAmount(room.getPrice());

        pgBookingRepo.save(booking);
    }

    @Override
    public void approve(Long bookingId) {
        PgBooking booking = pgBookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("APPROVED");
        pgBookingRepo.save(booking);
    }

    @Override
    public void pay(Long bookingId, String customerEmail) {
        PgBooking booking = pgBookingRepo.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        walletService.transfer(
                customerEmail,
                booking.getOwnerEmail(),
                booking.getMonthlyAmount()
        );
    }

    @Override
    public List<PgBooking> getAllBookings() {
        return pgBookingRepo.findAll();
    }

    // ================= NEW METHODS =================

    @Override
    public Property getProperty(Long propertyId) {
        return propertyRepo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    @Override
    public List<Room> getRoomsByProperty(Long propertyId) {
        return roomRepo.findByPropertyId(propertyId);
    }
}