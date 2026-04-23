package com.realestate.service;

public interface WalletService {

    void addMoney(String email, Double amount);

    void transfer(String from, String to, Double amount);
}