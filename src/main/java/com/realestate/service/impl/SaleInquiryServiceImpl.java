package com.realestate.service.impl;

import com.realestate.entity.NegotiationMessage;
import com.realestate.entity.Property;
import com.realestate.entity.SaleInquiry;
import com.realestate.enums.PropertyStatus;
import com.realestate.repository.NegotiationRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.SaleInquiryRepository;
import com.realestate.service.SaleInquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleInquiryServiceImpl implements SaleInquiryService {

    private final NegotiationRepository negotiationRepo;
    private final SaleInquiryRepository saleInquiryRepository;
    private final PropertyRepository propertyRepository;

    @Override
    public void createInquiry(Long propertyId, String email, BigDecimal amount, String message) {
        System.out.println("🔥🔥🔥 CREATE INQUIRY METHOD CALLED 🔥🔥🔥");
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (property.getOwner() == null) {
            throw new RuntimeException("Property owner not found");
        }

        if (property.getOwner().getEmail() == null || property.getOwner().getEmail().isBlank()) {
            throw new RuntimeException("Property owner email not found");
        }

        String ownerEmail = property.getOwner().getEmail().trim().toLowerCase();

        System.out.println("DEBUG 1 -> Property ID = " + property.getId());
        System.out.println("DEBUG 2 -> Property Owner ID = " + property.getOwner().getId());
        System.out.println("DEBUG 3 -> Owner Email Saved = " + ownerEmail);
        System.out.println("DEBUG 4 -> Customer Email = " + email);

        SaleInquiry inquiry = new SaleInquiry();
        inquiry.setProperty(property);
        inquiry.setCustomerEmail(email);
        inquiry.setOwnerEmail(ownerEmail);
        inquiry.setOfferAmount(amount);
        inquiry.setMessage(message);
        inquiry.setStatus("ACTIVE");
        inquiry.setTokenAmount(new BigDecimal("10000"));
        inquiry.setTokenPaid(false);
        inquiry.setCreatedAt(LocalDateTime.now());

        System.out.println("DEBUG 5 -> Inquiry ownerEmail before save = " + inquiry.getOwnerEmail());

        saleInquiryRepository.save(inquiry);

        System.out.println("DEBUG 6 -> Inquiry saved with ID = " + inquiry.getId());
    }

    @Override
    public void updateStatus(Long inquiryId, String status) {
        SaleInquiry inquiry = saleInquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        inquiry.setStatus(status);
        saleInquiryRepository.save(inquiry);
    }

    @Override
    public List<SaleInquiry> getOwnerInquiries(String email) {
        return saleInquiryRepository.findByOwnerEmail(email.trim().toLowerCase());
    }

    @Override
    public List<SaleInquiry> getCustomerInquiries(String email) {
        return saleInquiryRepository.findByCustomerEmail(email);
    }

    @Override
    public List<SaleInquiry> getAllInquiries() {
        return saleInquiryRepository.findAll();
    }

    @Override
    public void payToken(Long inquiryId, String customerEmail) {
        SaleInquiry inquiry = saleInquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        if (!inquiry.getCustomerEmail().equalsIgnoreCase(customerEmail)) {
            throw new RuntimeException("Not allowed");
        }

        if (!"ACCEPTED".equalsIgnoreCase(inquiry.getStatus())) {
            throw new RuntimeException("Only accepted deals allowed");
        }

        inquiry.setTokenPaid(true);
        inquiry.setStatus("BOOKED");
        saleInquiryRepository.save(inquiry);

        Property property = inquiry.getProperty();
        property.setStatus(PropertyStatus.SOLD);
        propertyRepository.save(property);
    }

    @Override
    public void sendMessage(Long inquiryId, String sender, String msg) {

        SaleInquiry inquiry = saleInquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Not found"));

        NegotiationMessage message = new NegotiationMessage();
        message.setInquiry(inquiry);
        message.setSender(sender);
        message.setMessage(msg);
        message.setCreatedAt(LocalDateTime.now());

        negotiationRepo.save(message);
    }

    @Override
    public List<NegotiationMessage> getMessages(Long inquiryId) {
        return negotiationRepo.findByInquiryId(inquiryId);
    }

    @Override
    public void acceptDeal(Long id) {

        SaleInquiry inquiry = saleInquiryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));

        inquiry.setStatus("ACCEPTED");
        inquiry.setDealClosed(true);

        saleInquiryRepository.save(inquiry);
    }

    @Override
    public SaleInquiry getById(Long inquiryId) {
        return saleInquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
    }
}