package com.realestate.service.impl;

import com.realestate.entity.User;
import com.realestate.repository.UserRepository;
import com.realestate.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepo;

    @Override
    public void addMoney(String email, Double amount) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        double currentWallet = user.getWallet() == null ? 0.0 : user.getWallet();

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        user.setWallet(currentWallet + amount);
        userRepo.save(user);
    }

    @Override
    public void transfer(String from, String to, Double amount) {

        User sender = userRepo.findByEmail(from)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        User receiver = userRepo.findByEmail(to)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        double senderWallet = sender.getWallet() == null ? 0.0 : sender.getWallet();
        double receiverWallet = receiver.getWallet() == null ? 0.0 : receiver.getWallet();

        if (amount == null || amount <= 0) {
            throw new RuntimeException("Amount must be greater than 0");
        }

        if (senderWallet < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setWallet(senderWallet - amount);
        receiver.setWallet(receiverWallet + amount);

        userRepo.save(sender);
        userRepo.save(receiver);
    }
}