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
    public String createOrder(@RequestParam Double amount) {
        return paymentService.createOrder(amount);
    }

    @PostMapping("/verify-payment")
    public String verifyPayment(@RequestParam String razorpay_payment_id,
                                @RequestParam String razorpay_order_id,
                                @RequestParam String razorpay_signature,
                                @RequestParam Double amount,
                                Principal principal) {

        boolean isValid = paymentService.verifySignature(
                razorpay_order_id,
                razorpay_payment_id,
                razorpay_signature
        );

        if (!isValid) {
            throw new RuntimeException("Payment verification failed");
        }

        walletService.addMoney(principal.getName(), amount);

        return "success";
    }
}