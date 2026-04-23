package com.realestate.controller;

import com.realestate.entity.SaleInquiry;
import com.realestate.entity.User;
import com.realestate.repository.SaleInquiryRepository;
import com.realestate.repository.UserRepository;
import com.realestate.service.SaleInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class CustomerInquiryController {

    private final SaleInquiryRepository inquiryRepo;
    private final SaleInquiryService inquiryService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/customer/inquiries")
    public String customerInquiries(Model model, Principal principal) {
        model.addAttribute("inquiries", inquiryRepo.findByCustomerEmail(principal.getName()));
        return "customer/inquiry-list";
    }

    @GetMapping("/customer/inquiry/{id}")
    public String viewNegotiation(@PathVariable Long id, Model model) {
        model.addAttribute("inquiry", inquiryService.getById(id));
        model.addAttribute("messages", inquiryService.getMessages(id));
        return "customer/negotiation";
    }

    @PostMapping("/customer/inquiry/message")
    public String sendMessage(@RequestParam Long inquiryId,
                              @RequestParam String message,
                              Principal principal) {
        inquiryService.sendMessage(inquiryId, "CUSTOMER", message);
        return "redirect:/customer/inquiry/" + inquiryId;
    }

    @PostMapping("/customer/inquiry/payment/token")
    public String payToken(@RequestParam Long inquiryId) {
        SaleInquiry inquiry = inquiryRepo.findById(inquiryId).orElseThrow();

        inquiry.setPaymentId("PAY" + System.currentTimeMillis());
        inquiry.setTokenAmount(new BigDecimal("10000"));
        inquiry.setStatus("CLOSED");

        inquiryRepo.save(inquiry);

        return "redirect:/customer/dashboard";
    }

    @PostMapping("/customer/inquiry/accept")
    public String accept(@RequestParam Long inquiryId) {
        inquiryService.acceptDeal(inquiryId);
        return "redirect:/owner/inquiries";
    }

    @GetMapping("/customer/wallet")
    public String walletPage(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "customer/wallet";
    }

    @GetMapping("/customer/rent-bookings")
    public String rentBookingsPage() {
        return "customer/rent-bookings";
    }

    @GetMapping("/customer/pg-bookings")
    public String pgBookingsPage() {
        return "customer/pg-bookings";
    }

    @GetMapping("/customer/profile")
    public String customerProfile(Model model, Principal principal) {
        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        model.addAttribute("user", user);
        return "customer/profile";
    }

    @PostMapping("/customer/profile/update")
    public String updateProfile(@RequestParam String firstName,
                                @RequestParam(required = false) String lastName,
                                @RequestParam(required = false) String contactNo,
                                @RequestParam(required = false) String password,
                                @RequestParam(required = false) MultipartFile profileImage,
                                Principal principal,
                                Model model) {

        User user = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setContactNo(contactNo);

        if (password != null && !password.isBlank()) {
            user.setPassword(passwordEncoder.encode(password));
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            try {
                String originalFilename = profileImage.getOriginalFilename();
                String extension = "";

                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                String fileName = UUID.randomUUID() + extension;

                Path uploadDir = Paths.get("uploads/profile");
                Files.createDirectories(uploadDir);

                Path filePath = uploadDir.resolve(fileName);
                Files.copy(profileImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                user.setProfileImageUrl("/uploads/profile/" + fileName);
            } catch (Exception e) {
                throw new RuntimeException("Failed to upload profile image", e);
            }
        }

        userRepository.save(user);

        model.addAttribute("user", user);
        model.addAttribute("success", true);

        return "customer/profile";
    }
}