package com.realestate.controller;

import com.realestate.dto.PropertyDto;
import com.realestate.entity.Property;
import com.realestate.service.LocationService;
import com.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner/properties")
public class OwnerPropertyController {

    private final PropertyService propertyService;
    private final LocationService locationService;

    // 📌 Show all owner properties
    @GetMapping
    public String viewProperties(Authentication auth, Model model) {
        String email = auth.getName();
        List<Property> properties = propertyService.getOwnerProperties(email);
        model.addAttribute("properties", properties);
        return "owner/property-list";
    }

    // 📌 Add Property Page
    @GetMapping("/add")
    public String addPropertyPage(Model model) {
        model.addAttribute("propertyDto", new PropertyDto());
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("pageMode", "add");
        return "owner/property-form";
    }

    // 📌 Save Property
    @PostMapping("/add")
    public String saveProperty(@ModelAttribute PropertyDto propertyDto,
                               @RequestParam("image") MultipartFile image,
                               Authentication auth) {

        String email = auth.getName();
        propertyService.addProperty(email, propertyDto, image);

        return "redirect:/owner/properties?success=added";
    }

    // 📌 Edit Property Page
    @GetMapping("/edit/{id}")
    public String editPropertyPage(@PathVariable Long id, Model model) {
        Property property = propertyService.getPropertyById(id);

        PropertyDto propertyDto = new PropertyDto();
        propertyDto.setId(property.getId());
        propertyDto.setName(property.getName());
        propertyDto.setDescription(property.getDescription());
        propertyDto.setAddress(property.getAddress());

        if (property.getLocation() != null) {
            propertyDto.setLocationId(property.getLocation().getId());
        }

        propertyDto.setListingType(property.getListingType());
        propertyDto.setPropertyType(property.getPropertyType());
        propertyDto.setPgListing(property.isPgListing());
        propertyDto.setPrice(property.getPrice());
        propertyDto.setSizeSqFt(property.getSizeSqFt());
        propertyDto.setBhk(property.getBhk());

        model.addAttribute("propertyDto", propertyDto);
        model.addAttribute("locations", locationService.getAllLocations());
        model.addAttribute("pageMode", "edit");
        model.addAttribute("propertyId", id);

        return "owner/property-form";
    }

    // 📌 Update Property
    @PostMapping("/edit/{id}")
    public String updateProperty(@PathVariable Long id,
                                 @ModelAttribute PropertyDto propertyDto,
                                 @RequestParam(value = "image", required = false) MultipartFile image,
                                 Authentication auth) {

        String email = auth.getName();
        propertyService.updateProperty(id, email, propertyDto, image);

        return "redirect:/owner/properties?success=updated";
    }

    // 📌 Delete Property
    @GetMapping("/delete/{id}")
    public String deleteProperty(@PathVariable Long id, Authentication auth) {
        String email = auth.getName();
        propertyService.deleteProperty(id, email);
        return "redirect:/owner/properties?success=deleted";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Working!";
    }
}