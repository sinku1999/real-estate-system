package com.realestate.controller;

import com.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import com.realestate.entity.Property;

@Controller
@RequiredArgsConstructor
public class OwnerExtraController {

    private final PropertyService propertyService;   // ✅ IMPORTANT



    @GetMapping("/owner/rent-bookings")
    public String rentBookings() {
        return "owner/rent-bookings";
    }

    @GetMapping("/owner/explore")
    public String ownerExplore(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location,
            Model model) {

        List<Property> properties;

        // 🔍 FILTER LOGIC
        if (keyword != null && !keyword.isEmpty()) {
            properties = propertyService.searchByName(keyword);
        } else if (location != null && !location.isEmpty()) {
            properties = propertyService.findByLocation(location);
        } else {
            properties = propertyService.getAll();
        }

        model.addAttribute("properties", properties);
        model.addAttribute("locations", propertyService.getAllLocations());

        return "owner/explore";
    }
}