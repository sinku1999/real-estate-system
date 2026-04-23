package com.realestate.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class OwnerProfileDto {

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be less than or equal to 100")
    private Integer age;

    @NotBlank(message = "Profession is required")
    @Size(max = 100, message = "Profession must be under 100 characters")
    private String profession;

    @NotBlank(message = "Address is required")
    @Size(max = 400, message = "Address must be under 400 characters")
    private String address;

    private MultipartFile govtProofFile;
}