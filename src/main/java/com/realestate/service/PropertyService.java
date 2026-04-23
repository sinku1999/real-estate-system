package com.realestate.service;

import com.realestate.dto.PropertyDto;
import com.realestate.entity.Property;
import com.realestate.enums.PropertyStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PropertyService {

    List<Property> searchByName(String keyword);

    List<Property> findByLocation(String location);

    List<String> getAllLocations();

    List<Property> getAll();

    Property addProperty(String ownerEmail, PropertyDto propertyDto, MultipartFile image);

    Property updateProperty(Long id, String ownerEmail, PropertyDto propertyDto, MultipartFile image);

    void deleteProperty(Long id, String ownerEmail);

    List<Property> getOwnerProperties(String ownerEmail);

    List<Property> getOwnerPropertiesByStatus(String ownerEmail, PropertyStatus status);

    long countOwnerProperties(String ownerEmail);

    List<Property> getAllPropertiesByStatus(PropertyStatus status);

    Property updatePropertyStatus(Long propertyId, PropertyStatus status);

    List<Property> searchApprovedProperties(String keyword);

    long countPropertiesByStatus(PropertyStatus status);

    Property getPropertyById(Long id);
}