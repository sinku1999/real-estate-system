package com.realestate.controller;

import com.realestate.entity.PgBooking;
import com.realestate.service.PgService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PgController {

    private final PgService pgService;

    // ================= ADMIN =================
    @GetMapping("/admin/pg-bookings")
    public String adminPgBookings(Model model) {
        model.addAttribute("bookings", pgService.getAllBookings());
        return "admin/pg-bookings";
    }

    // ================= OWNER =================
    @GetMapping("/owner/pg-bookings")
    public String ownerPgBookings(Model model, Principal principal) {

        String ownerEmail = principal.getName();

        List<PgBooking> ownerBookings = pgService.getAllBookings()
                .stream()
                .filter(b -> b.getOwnerEmail() != null && b.getOwnerEmail().equalsIgnoreCase(ownerEmail))
                .toList();

        model.addAttribute("bookings", ownerBookings);

        return "owner/pg-bookings";
    }

    // ================= CUSTOMER ROOMS PAGE =================
    @GetMapping("/customer/property/{id}/rooms")
    public String viewRooms(@PathVariable Long id, Model model) {
        model.addAttribute("property", pgService.getProperty(id));
        model.addAttribute("rooms", pgService.getRoomsByProperty(id));
        return "customer/pg-rooms";
    }

    // ================= CUSTOMER BOOK PG =================
    @PostMapping("/pg/book")
    public String bookPg(@RequestParam Long roomId,
                         Principal principal) {

        pgService.createBooking(roomId, principal.getName());

        return "redirect:/customer/pg-bookings";
    }

    // ================= OWNER APPROVE =================
    @PostMapping("/owner/pg/approve")
    public String approvePg(@RequestParam Long bookingId) {

        pgService.approve(bookingId);

        return "redirect:/owner/pg-bookings";
    }

    // ================= CUSTOMER PAY =================
    @PostMapping("/pg/pay")
    public String payPg(@RequestParam Long bookingId,
                        Principal principal) {

        pgService.pay(bookingId, principal.getName());

        return "redirect:/customer/pg-bookings";
    }
}