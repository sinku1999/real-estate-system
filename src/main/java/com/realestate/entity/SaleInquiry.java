package com.realestate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ADD THIS FIELD
    private Boolean dealClosed = false;

    // ADD TOKEN PAYMENT
    private BigDecimal tokenAmount;
    private String paymentId;

    private BigDecimal offerAmount;

    private String message;

    private String status; // ACTIVE / ACCEPTED / REJECTED / CANCELLED / BOOKED

//    private BigDecimal tokenAmount;

    private boolean tokenPaid;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne
    private Property property;

    private String customerEmail;

    private String ownerEmail;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        if (this.status == null) {
            this.status = "ACTIVE";
        }

        if (this.tokenAmount == null) {
            this.tokenAmount = new BigDecimal("10000");
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}