package com.realestate.controller;

import com.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer")
public class CustomerPropertyController {

    private final PropertyService propertyService;

    // ✅ Property details REMOVED from here
    // Now handled by PublicController at /property/{id}

}