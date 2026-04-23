package com.realestate.controller;

import com.realestate.service.SaleInquiryService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class SaleInquiryController {

    private final SaleInquiryService saleInquiryService;

    public SaleInquiryController(SaleInquiryService saleInquiryService) {
        this.saleInquiryService = saleInquiryService;
    }

    @GetMapping("/admin/sale-inquiries")
    public String adminSaleInquiries(Model model) {
        model.addAttribute("inquiries", saleInquiryService.getAllInquiries());
        return "admin/sale-inquiries";
    }

    @PostMapping("/inquiry")
    public String createInquiry(@RequestParam Long propertyId,
                                @RequestParam BigDecimal amount,
                                @RequestParam String message,
                                Authentication auth) {

        saleInquiryService.createInquiry(propertyId, auth.getName(), amount, message);

        return "redirect:/explore";
    }
}