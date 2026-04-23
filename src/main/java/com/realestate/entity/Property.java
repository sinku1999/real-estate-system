package com.realestate.entity;

import com.realestate.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double rent;

    private String name;

    @Column(length = 1000)
    private String description;

    private String address;

    private String bhk;

    private Integer sizeSqFt;

    private String imageUrl;

    private String imagePath;

    private boolean pgListing;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private ListingType listingType;

    @Enumerated(EnumType.STRING)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    private PriceType priceType;

    @Enumerated(EnumType.STRING)
    private PropertyStatus status;

    // ✅ ONLY THIS SHOULD EXIST
    @ManyToOne
    private User owner;

    @ManyToOne
    private Location location;
}