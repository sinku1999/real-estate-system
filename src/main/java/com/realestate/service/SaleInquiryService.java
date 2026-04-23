package com.realestate.service;

import com.realestate.entity.SaleInquiry;
import com.realestate.entity.NegotiationMessage;

import java.math.BigDecimal;
import java.util.List;

public interface SaleInquiryService {

    void createInquiry(Long propertyId, String email, BigDecimal amount, String message);

    void updateStatus(Long inquiryId, String status);

    List<SaleInquiry> getOwnerInquiries(String email);

    List<SaleInquiry> getCustomerInquiries(String email);

    List<SaleInquiry> getAllInquiries();

    void payToken(Long inquiryId, String customerEmail);

    void sendMessage(Long inquiryId, String sender, String message);

    List<NegotiationMessage> getMessages(Long inquiryId);

    void acceptDeal(Long inquiryId);

    SaleInquiry getById(Long inquiryId);
}