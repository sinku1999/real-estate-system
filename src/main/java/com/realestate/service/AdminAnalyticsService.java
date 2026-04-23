package com.realestate.service;

import com.realestate.service.PropertyService;
import com.realestate.repository.LocationRepository;
import com.realestate.enums.PropertyStatus;

public interface AdminAnalyticsService {
    Long totalProperties();
    Long totalUsers();
    Long totalBookings();
}