package com.realestate.controller;

import com.realestate.entity.User;
import com.realestate.repository.UserRepository;
import com.realestate.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserRepository userRepository;
    private final EmailService emailService;

    @GetMapping("/dashboard")
    public String dashboardRedirect(Authentication authentication) {

        if (authentication == null || authentication.getAuthorities() == null) {
            return "redirect:/login";
        }

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean isOwner = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_OWNER"));

        boolean isCustomer = authentication.getAuthorities()
                .stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_CUSTOMER"));

        if (isAdmin) {
            return "redirect:/admin/dashboard";
        } else if (isOwner) {
            return "redirect:/owner/dashboard";
        } else if (isCustomer) {
            return "redirect:/customer/dashboard";
        }

        return "redirect:/";
    }

    @GetMapping("/owner/dashboard")
    public String ownerDashboard(Model model) {
        model.addAttribute("myProperties", 0);
        model.addAttribute("pendingProperties", 0);
        model.addAttribute("approvedProperties", 0);
        model.addAttribute("rejectedProperties", 0);
        return "owner/dashboard";
    }

    @GetMapping("/customer/dashboard")
    public String customerDashboard(Model model, Principal principal) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);

        return "customer/dashboard";
    }
}