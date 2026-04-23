package com.realestate.controller;

import com.realestate.entity.Property;
import com.realestate.service.PropertyService;
import com.realestate.service.RentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class RentController {

    private final RentService rentService;
    private final PropertyService propertyService;

    @GetMapping("/admin/rent-bookings")
    public String adminRentBookings(Model model) {
        model.addAttribute("bookings", rentService.getAllBookings());
        return "admin/rent-bookings";
    }

    @GetMapping("/customer/property/{id}/book")
    public String showBookingPage(@PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);
        model.addAttribute("property", property);
        return "customer/rent-booking";
    }

    @PostMapping("/rent/book")
    public String bookRent(@RequestParam Long propertyId,
                           Principal principal) {

        rentService.createBooking(propertyId, principal.getName());
        return "redirect:/customer/dashboard";
    }

    @PostMapping("/owner/rent/approve")
    public String approveRent(@RequestParam Long bookingId) {
        rentService.approve(bookingId);
        return "redirect:/owner/bookings";
    }

    @PostMapping("/rent/pay")
    public String payRent(@RequestParam Long bookingId,
                          Principal principal) {

        rentService.pay(bookingId, principal.getName());
        return "redirect:/customer/dashboard";
    }
}