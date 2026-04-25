package com.realestate.service.impl;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.realestate.service.PaymentService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${razorpay.key}")
    private String key;

    @Value("${razorpay.secret}")
    private String secret;

    @Override
    public String createOrder(Double amount) {

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Invalid amount");
        }

        try {
            RazorpayClient client = new RazorpayClient(key, secret);

            JSONObject options = new JSONObject();
            options.put("amount", (int) Math.round(amount * 100));
            options.put("currency", "INR");
            options.put("receipt", "rcpt_" + System.currentTimeMillis());

            Order order = client.orders.create(options);

            JSONObject response = new JSONObject();
            response.put("key", key);
            response.put("id", String.valueOf(order.get("id")));
            response.put("amount", Integer.parseInt(String.valueOf(order.get("amount"))));
            response.put("currency", String.valueOf(order.get("currency")));

            return response.toString();

        } catch (Exception e) {
            throw new RuntimeException("Failed to create Razorpay order: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean verifySignature(String razorpayOrderId,
                                   String razorpayPaymentId,
                                   String razorpaySignature) {

        try {
            String data = razorpayOrderId + "|" + razorpayPaymentId;

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    "HmacSHA256"
            );
            mac.init(secretKey);

            byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                String s = Integer.toHexString(0xff & b);
                if (s.length() == 1) {
                    hex.append('0');
                }
                hex.append(s);
            }

            return hex.toString().equals(razorpaySignature);

        } catch (Exception e) {
            throw new RuntimeException("Signature verification failed: " + e.getMessage(), e);
        }
    }
}