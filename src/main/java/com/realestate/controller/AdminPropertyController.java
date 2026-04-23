package com.realestate.controller;

import com.realestate.entity.Property;
import com.realestate.enums.PropertyStatus;
import com.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/properties")
public class AdminPropertyController {

    private final PropertyService propertyService;

    @GetMapping
    public String viewProperties(@RequestParam(value = "status", required = false) String status, Model model) {
        PropertyStatus propertyStatus;

        if (status == null || status.isBlank()) {
            propertyStatus = PropertyStatus.PENDING;
        } else {
            propertyStatus = PropertyStatus.valueOf(status.toUpperCase());
        }

        List<Property> properties = propertyService.getAllPropertiesByStatus(propertyStatus);

        model.addAttribute("properties", properties);
        model.addAttribute("activeStatus", propertyStatus.name());

        return "admin/property-list";
    }

    @GetMapping("/approve/{id}")
    public String approveProperty(@PathVariable Long id) {
        propertyService.updatePropertyStatus(id, PropertyStatus.APPROVED);
        return "redirect:/admin/properties?status=PENDING&success=approved";
    }

    @GetMapping("/reject/{id}")
    public String rejectProperty(@PathVariable Long id) {
        propertyService.updatePropertyStatus(id, PropertyStatus.REJECTED);
        return "redirect:/admin/properties?status=PENDING&success=rejected";
    }
}