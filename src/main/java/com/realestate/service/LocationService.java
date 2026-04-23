package com.realestate.service;

import com.realestate.dto.LocationDto;
import com.realestate.entity.Location;

import java.util.List;

public interface LocationService {

    Location addLocation(LocationDto locationDto);

    List<Location> getAllLocations();

    Location getLocationById(Long id);

    Location updateLocation(Long id, LocationDto locationDto);

    void deleteLocation(Long id);
}