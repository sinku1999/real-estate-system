package com.realestate.service.impl;

import com.realestate.dto.OwnerProfileDto;
import com.realestate.entity.OwnerProfile;
import com.realestate.entity.User;
import com.realestate.repository.OwnerProfileRepository;
import com.realestate.repository.UserRepository;
import com.realestate.service.OwnerProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OwnerProfileServiceImpl implements OwnerProfileService {

    private final OwnerProfileRepository ownerProfileRepository;
    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public OwnerProfile saveOrUpdateProfile(String email, OwnerProfileDto ownerProfileDto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        OwnerProfile ownerProfile = ownerProfileRepository.findByUser(user)
                .orElse(OwnerProfile.builder().user(user).build());

        ownerProfile.setAge(ownerProfileDto.getAge());
        ownerProfile.setProfession(ownerProfileDto.getProfession());
        ownerProfile.setAddress(ownerProfileDto.getAddress());

        MultipartFile govtProofFile = ownerProfileDto.getGovtProofFile();
        if (govtProofFile != null && !govtProofFile.isEmpty()) {
            String savedFileName = saveFile(govtProofFile);
            ownerProfile.setGovtProofPath("/uploads/" + savedFileName);
        }

        return ownerProfileRepository.save(ownerProfile);
    }

    @Override
    public OwnerProfile getProfileByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found."));

        return ownerProfileRepository.findByUser(user).orElse(null);
    }

    @Override
    public boolean profileExists(String email) {
        return getProfileByUserEmail(email) != null;
    }

    private String saveFile(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String fileName = UUID.randomUUID() + extension;

        File uploadPath = new File(uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        try {
            file.transferTo(new File(uploadPath, fileName));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file: " + e.getMessage());
        }

        return fileName;
    }
}