package com.realestate.controller;

import com.realestate.dto.OwnerProfileDto;
import com.realestate.entity.OwnerProfile;
import com.realestate.entity.User;
import com.realestate.repository.UserRepository;
import com.realestate.service.OwnerProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/owner/profile")
public class OwnerProfileController {

    private final OwnerProfileService ownerProfileService;
    private final UserRepository userRepository;

    @GetMapping
    public String viewProfile(Authentication authentication, Model model) {
        String email = authentication.getName();

        OwnerProfile profile = ownerProfileService.getProfileByUserEmail(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("profile", profile);
        model.addAttribute("user", user);

        return "owner/profile-view";
    }

    @GetMapping("/edit")
    public String profileForm(Authentication authentication, Model model) {
        String email = authentication.getName();

        OwnerProfile profile = ownerProfileService.getProfileByUserEmail(email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        OwnerProfileDto dto = new OwnerProfileDto();

        if (profile != null) {
            dto.setAge(profile.getAge());
            dto.setProfession(profile.getProfession());
            dto.setAddress(profile.getAddress());
        }

        model.addAttribute("ownerProfileDto", dto);
        model.addAttribute("user", user);

        return "owner/profile-form";
    }

    @PostMapping("/edit")
    public String saveProfile(Authentication authentication,
                              @Valid @ModelAttribute("ownerProfileDto") OwnerProfileDto ownerProfileDto,
                              BindingResult result,
                              @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                              Model model) {

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "owner/profile-form";
        }

        try {
            if (profileImage != null && !profileImage.isEmpty()) {
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
                userRepository.save(user);
            }

            ownerProfileService.saveOrUpdateProfile(email, ownerProfileDto);
            return "redirect:/owner/profile?success=saved";

        } catch (RuntimeException e) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", e.getMessage());
            return "owner/profile-form";
        } catch (Exception e) {
            model.addAttribute("user", user);
            model.addAttribute("errorMessage", "Failed to upload profile image");
            return "owner/profile-form";
        }
    }
}