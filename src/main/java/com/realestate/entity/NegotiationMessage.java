package com.realestate.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NegotiationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender; // CUSTOMER / OWNER
    private String message;

    private LocalDateTime createdAt;

    @ManyToOne
    private SaleInquiry inquiry;
}