package com.realestate.controller;

import com.realestate.entity.SaleInquiry;
import com.realestate.entity.User;
import com.realestate.repository.UserRepository;
import com.realestate.service.SaleInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner/inquiries")
public class OwnerInquiryController {

    private final SaleInquiryService saleInquiryService;
    private final UserRepository userRepository;

    // View inquiries
    @GetMapping
    public String viewInquiries(Authentication auth, Model model) {

        String loginValue = auth.getName();

        User user = userRepository.findByEmail(loginValue)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SaleInquiry> inquiries = saleInquiryService.getOwnerInquiries(user.getEmail());

        model.addAttribute("inquiries", inquiries);

        return "owner/inquiry-list";
    }

    // Accept inquiry
    @GetMapping("/accept/{id}")
    public String accept(@PathVariable Long id) {
        saleInquiryService.updateStatus(id, "ACCEPTED");
        return "redirect:/owner/inquiries";
    }

    // Reject inquiry
    @GetMapping("/reject/{id}")
    public String reject(@PathVariable Long id) {
        saleInquiryService.updateStatus(id, "REJECTED");
        return "redirect:/owner/inquiries";
    }
}