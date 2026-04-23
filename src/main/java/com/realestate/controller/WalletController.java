package com.realestate.controller;

import com.realestate.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/wallet")
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/add")
    public String addMoney(@RequestParam(required = false) Double amount, Principal principal) {

        if (amount == null || amount <= 0) {
            return "redirect:/customer/dashboard";
        }

        walletService.addMoney(principal.getName(), amount);

        return "redirect:/customer/dashboard";
    }
}