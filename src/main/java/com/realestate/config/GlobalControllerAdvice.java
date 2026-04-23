package com.realestate.config;

import com.realestate.entity.User;
import com.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {

    private final UserRepository userRepository;

    @ModelAttribute("loggedInUser")
    public User getLoggedInUser(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        return userRepository.findByEmail(authentication.getName()).orElse(null);
    }

    @ModelAttribute("loggedInUserEmail")
    public String getLoggedInUserEmail(Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return null;
        }

        return authentication.getName();
    }
}