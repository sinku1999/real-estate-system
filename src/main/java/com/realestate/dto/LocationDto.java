package com.realestate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {

    private Long id;

    @NotBlank(message = "Location name is required")
    @Size(max = 100, message = "Location name must be under 100 characters")
    private String name;

    @Size(max = 300, message = "Description must be under 300 characters")
    private String description;
}