package com.realestate.service;

public interface PaymentService {
    String createOrder(Double amount);
    boolean verifySignature(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature);
}