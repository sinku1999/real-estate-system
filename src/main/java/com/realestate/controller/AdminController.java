package com.realestate.controller;

import com.realestate.enums.PropertyStatus;
import com.realestate.repository.LocationRepository;
import com.realestate.service.AdminAnalyticsService;
import com.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminAnalyticsService service;
    private final LocationRepository locationRepository;
    private final PropertyService propertyService;

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("totalProperties", service.totalProperties());
        model.addAttribute("totalUsers", service.totalUsers());
        model.addAttribute("totalBookings", service.totalBookings());

        model.addAttribute("totalLocations", locationRepository.count());
        model.addAttribute("pendingProperties", propertyService.countPropertiesByStatus(PropertyStatus.PENDING));
        model.addAttribute("approvedProperties", propertyService.countPropertiesByStatus(PropertyStatus.APPROVED));
        model.addAttribute("rejectedProperties", propertyService.countPropertiesByStatus(PropertyStatus.REJECTED));

        return "admin/dashboard";
    }
}