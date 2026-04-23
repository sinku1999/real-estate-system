package com.realestate.service.impl;

import com.realestate.repository.PropertyRepository;
import com.realestate.repository.RentBookingRepository;
import com.realestate.repository.UserRepository;
import com.realestate.service.AdminAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminAnalyticsServiceImpl implements AdminAnalyticsService {

    private final PropertyRepository propertyRepo;
    private final UserRepository userRepo;
    private final RentBookingRepository rentRepo;

    @Override
    public Long totalProperties() {
        return propertyRepo.count();
    }

    @Override
    public Long totalUsers() {
        return userRepo.count();
    }

    @Override
    public Long totalBookings() {
        return rentRepo.count();
    }
}