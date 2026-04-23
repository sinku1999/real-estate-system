package com.realestate.controller;

import com.realestate.entity.Property;
import com.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final PropertyService propertyService;

    @GetMapping("/explore")
    public String exploreProperties(@RequestParam(value = "keyword", required = false) String keyword,
                                    Model model) {

        List<Property> properties = propertyService.searchApprovedProperties(keyword);

        model.addAttribute("properties", properties);
        model.addAttribute("keyword", keyword);

        return "customer/explore-properties";
    }
    @GetMapping("/property/{id}")
    public String propertyDetail(@PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);
        model.addAttribute("property", property);
        return "customer/property-details";
    }
}