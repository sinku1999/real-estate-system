package com.realestate.controller;

import com.realestate.service.PaymentService;
import com.realestate.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final WalletService walletService;

    @PostMapping(value = "/create-order", produces = MediaType.APPLICATION_JSON_VALUE)
    public String createOrder(@RequestParam("amount") Double amount,
                              Principal principal) {

        if (principal == null || principal.getName() == null) {
            throw new RuntimeException("User not logged in");
        }

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        return paymentService.createOrder(amount);
    }

    @PostMapping(value = "/verify-payment", produces = MediaType.TEXT_PLAIN_VALUE)
    public String verifyPayment(@RequestParam("razorpay_payment_id") String razorpayPaymentId,
                                @RequestParam("razorpay_order_id") String razorpayOrderId,
                                @RequestParam("razorpay_signature") String razorpaySignature,
                                @RequestParam("amount") Double amount,
                                Principal principal) {

        if (principal == null || principal.getName() == null) {
            throw new RuntimeException("User not logged in");
        }

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        boolean isValid = paymentService.verifySignature(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature
        );

        if (!isValid) {
            throw new RuntimeException("Payment verification failed");
        }

        walletService.addMoney(principal.getName(), amount);

        return "success";
    }
}