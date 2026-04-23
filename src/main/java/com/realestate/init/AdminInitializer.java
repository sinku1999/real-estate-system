package com.realestate.init;

import com.realestate.entity.User;
import com.realestate.enums.Role;
import com.realestate.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        boolean adminExists = userRepository.existsByRole(Role.ADMIN);

        if (!adminExists) {
            User admin = User.builder()
                    .firstName("System")
                    .lastName("Admin")
                    .email("admin@realestate.com")
                    .contactNo("9999999999")
                    .password(passwordEncoder.encode("admin123"))
//                    .password("admin123")
                    .role(Role.ADMIN)
                    .walletAmount(BigDecimal.ZERO)
                    .enabled(true)
                    .build();

            userRepository.save(admin);

            System.out.println("Default admin created successfully.");
            System.out.println("Email: admin@realestate.com");
            System.out.println("Password: admin123");
        } else {
            System.out.println("Admin already exists.");
        }
    }
}