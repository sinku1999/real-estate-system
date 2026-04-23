package com.realestate.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class RentBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long propertyId;
    private String customerEmail;
    private String ownerEmail;

    private String status; // PENDING / APPROVED / REJECTED

    private Double monthlyRent;
    private Integer totalMonths;
}