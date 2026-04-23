package com.realestate.controller;

import com.realestate.dto.UserRegistrationDto;
import com.realestate.entity.User;
import com.realestate.enums.Role;
import com.realestate.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/")
    public String home() {
        return "public/index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register/owner")
    public String ownerRegisterPage(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "auth/register-owner";
    }

    @PostMapping("/register/owner")
    public String registerOwner(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return "auth/register-owner";
        }

        try {
            User user = new User();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setContactNo(userDto.getContactNo());
            user.setPassword(userDto.getPassword());

            userService.registerUser(user, Role.OWNER);
            model.addAttribute("successMessage", "Owner registered successfully.");
            model.addAttribute("userDto", new UserRegistrationDto());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "auth/register-owner";
    }

    @GetMapping("/register/customer")
    public String customerRegisterPage(Model model) {
        model.addAttribute("userDto", new UserRegistrationDto());
        return "auth/register-customer";
    }

    @PostMapping("/register/customer")
    public String registerCustomer(@Valid @ModelAttribute("userDto") UserRegistrationDto userDto,
                                   BindingResult result,
                                   Model model) {
        if (result.hasErrors()) {
            return "auth/register-customer";
        }

        try {
            User user = new User();
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setEmail(userDto.getEmail());
            user.setContactNo(userDto.getContactNo());
            user.setPassword(userDto.getPassword());

            userService.registerUser(user, Role.CUSTOMER);
            model.addAttribute("successMessage", "Customer registered successfully.");
            model.addAttribute("userDto", new UserRegistrationDto());
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }

        return "auth/register-customer";
    }
}