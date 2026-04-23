package com.realestate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long propertyId;

    private String type; // PRIVATE / SHARED

    private Double price;

    private Integer totalBeds;

    private boolean available = true; // ✅ ADD THIS
}