package com.realestate.service.impl;

import com.realestate.dto.LocationDto;
import com.realestate.entity.Location;
import com.realestate.repository.LocationRepository;
import com.realestate.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;

    @Override
    public Location addLocation(LocationDto locationDto) {
        if (locationRepository.existsByNameIgnoreCase(locationDto.getName().trim())) {
            throw new RuntimeException("Location already exists.");
        }

        Location location = Location.builder()
                .name(locationDto.getName().trim())
                .description(locationDto.getDescription())
                .active(true)
                .build();

        return locationRepository.save(location);
    }

    @Override
    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Override
    public Location getLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found."));
    }

    @Override
    public Location updateLocation(Long id, LocationDto locationDto) {
        Location existingLocation = getLocationById(id);

        String newName = locationDto.getName().trim();

        locationRepository.findByNameIgnoreCase(newName).ifPresent(location -> {
            if (!location.getId().equals(id)) {
                throw new RuntimeException("Another location with this name already exists.");
            }
        });

        existingLocation.setName(newName);
        existingLocation.setDescription(locationDto.getDescription());

        return locationRepository.save(existingLocation);
    }

    @Override
    public void deleteLocation(Long id) {
        Location location = getLocationById(id);
        locationRepository.delete(location);
    }
}