package com.realestate.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.realestate.dto.PropertyDto;
import com.realestate.entity.Location;
import com.realestate.entity.Property;
import com.realestate.entity.User;
import com.realestate.enums.PropertyStatus;
import com.realestate.repository.LocationRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.UserRepository;
import com.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final Cloudinary cloudinary;

    @Override
    public List<Property> searchByName(String keyword) {
        return propertyRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Property> findByLocation(String location) {
        return propertyRepository.findByLocation_NameContainingIgnoreCase(location);
    }

    @Override
    public List<String> getAllLocations() {
        return propertyRepository.findDistinctLocations();
    }

    @Override
    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    @Override
    public Property addProperty(String ownerEmail, PropertyDto propertyDto, MultipartFile image) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Location location = locationRepository.findById(propertyDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        String imageUrl = uploadToCloudinary(image);

        Property property = Property.builder()
                .name(propertyDto.getName())
                .description(propertyDto.getDescription())
                .address(propertyDto.getAddress())
                .bhk(propertyDto.getBhk())
                .sizeSqFt(propertyDto.getSizeSqFt())
                .price(propertyDto.getPrice())
                .listingType(propertyDto.getListingType())
                .propertyType(propertyDto.getPropertyType())
                .pgListing(propertyDto.isPgListing())
                .status(PropertyStatus.PENDING)
                .location(location)
                .owner(owner)
                .imageUrl(imageUrl)
                .build();

        return propertyRepository.save(property);
    }

    @Override
    public Property updateProperty(Long id, String ownerEmail, PropertyDto propertyDto, MultipartFile image) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (property.getOwner() == null || !property.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You are not allowed to update this property");
        }

        Location location = locationRepository.findById(propertyDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        property.setName(propertyDto.getName());
        property.setDescription(propertyDto.getDescription());
        property.setAddress(propertyDto.getAddress());
        property.setBhk(propertyDto.getBhk());
        property.setSizeSqFt(propertyDto.getSizeSqFt());
        property.setPrice(propertyDto.getPrice());
        property.setListingType(propertyDto.getListingType());
        property.setPropertyType(propertyDto.getPropertyType());
        property.setPgListing(propertyDto.isPgListing());
        property.setLocation(location);

        if (image != null && !image.isEmpty()) {
            property.setImageUrl(uploadToCloudinary(image));
        }

        return propertyRepository.save(property);
    }

    @Override
    public void deleteProperty(Long id, String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (property.getOwner() == null || !property.getOwner().getId().equals(owner.getId())) {
            throw new RuntimeException("You are not allowed to delete this property");
        }

        propertyRepository.delete(property);
    }

    @Override
    public List<Property> getOwnerProperties(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        return propertyRepository.findByOwner(owner);
    }

    @Override
    public List<Property> getOwnerPropertiesByStatus(String ownerEmail, PropertyStatus status) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        return propertyRepository.findByOwnerAndStatus(owner, status);
    }

    @Override
    public long countOwnerProperties(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        return propertyRepository.countByOwner(owner);
    }

    @Override
    public List<Property> getAllPropertiesByStatus(PropertyStatus status) {
        return propertyRepository.findByStatus(status);
    }

    @Override
    public Property updatePropertyStatus(Long propertyId, PropertyStatus status) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        property.setStatus(status);
        return propertyRepository.save(property);
    }

    @Override
    public List<Property> searchApprovedProperties(String keyword) {
        return propertyRepository.findByStatusAndNameContainingIgnoreCase(PropertyStatus.APPROVED, keyword);
    }

    @Override
    public long countPropertiesByStatus(PropertyStatus status) {
        return propertyRepository.countByStatus(status);
    }

    @Override
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    private String uploadToCloudinary(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "estateflow/properties",
                            "resource_type", "image"
                    )
            );

            Object secureUrl = uploadResult.get("secure_url");

            if (secureUrl == null) {
                throw new RuntimeException("Cloudinary did not return image URL");
            }

            return secureUrl.toString();

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed: " + e.getMessage(), e);
        }
    }
}