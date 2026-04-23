package com.realestate.service.impl;

import com.realestate.entity.User;
import com.realestate.enums.Role;
import com.realestate.repository.UserRepository;
import com.realestate.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(User user, Role role) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(role);
        user.setWalletAmount(BigDecimal.ZERO);
        user.setEnabled(true);

        return userRepository.save(user);
    }
}