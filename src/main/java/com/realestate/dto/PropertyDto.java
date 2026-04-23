package com.realestate.dto;

import com.realestate.enums.ListingType;
import com.realestate.enums.PropertyType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PropertyDto {

    private Long id;

    @NotBlank(message = "Property name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Address is required")
    private String address;

    @NotNull(message = "Location is required")
    private Long locationId;

    @NotNull(message = "Listing type is required")
    private ListingType listingType;

    @NotNull(message = "Property type is required")
    private PropertyType propertyType;

    private boolean pgListing;

    private BigDecimal price;

    private Integer sizeSqFt;

    private String bhk;
}