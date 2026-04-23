package com.realestate.controller;

import com.realestate.dto.LocationDto;
import com.realestate.entity.Location;
import com.realestate.service.LocationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/locations")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public String viewLocations(Model model) {
        List<Location> locations = locationService.getAllLocations();
        model.addAttribute("locations", locations);
        return "admin/location-list";
    }

    @GetMapping("/add")
    public String addLocationPage(Model model) {
        model.addAttribute("locationDto", new LocationDto());
        model.addAttribute("pageMode", "add");
        return "admin/location-form";
    }

    @PostMapping("/add")
    public String saveLocation(@Valid @ModelAttribute("locationDto") LocationDto locationDto,
                               BindingResult result,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageMode", "add");
            return "admin/location-form";
        }

        try {
            locationService.addLocation(locationDto);
            return "redirect:/admin/locations?success=added";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageMode", "add");
            return "admin/location-form";
        }
    }

    @GetMapping("/edit/{id}")
    public String editLocationPage(@PathVariable Long id, Model model) {
        Location location = locationService.getLocationById(id);

        LocationDto locationDto = new LocationDto();
        locationDto.setId(location.getId());
        locationDto.setName(location.getName());
        locationDto.setDescription(location.getDescription());

        model.addAttribute("locationDto", locationDto);
        model.addAttribute("pageMode", "edit");
        return "admin/location-form";
    }

    @PostMapping("/edit/{id}")
    public String updateLocation(@PathVariable Long id,
                                 @Valid @ModelAttribute("locationDto") LocationDto locationDto,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            model.addAttribute("pageMode", "edit");
            return "admin/location-form";
        }

        try {
            locationService.updateLocation(id, locationDto);
            return "redirect:/admin/locations?success=updated";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageMode", "edit");
            return "admin/location-form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return "redirect:/admin/locations?success=deleted";
    }
}