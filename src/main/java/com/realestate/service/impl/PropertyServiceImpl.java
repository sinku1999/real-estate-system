package com.realestate.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.realestate.dto.PropertyDto;
import com.realestate.entity.Location;
import com.realestate.entity.Property;
import com.realestate.entity.User;
import com.realestate.enums.*;
import com.realestate.repository.LocationRepository;
import com.realestate.repository.PropertyRepository;
import com.realestate.repository.UserRepository;
import com.realestate.service.PropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    // ================= ADD =================
    @Override
    public Property addProperty(String ownerEmail, PropertyDto propertyDto, MultipartFile image) {

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found."));

        Location location = locationRepository.findById(propertyDto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found."));

        boolean isPg = propertyDto.isPgListing();

        ListingType listingType = propertyDto.getListingType();
        PropertyType propertyType = propertyDto.getPropertyType();
        PriceType priceType;
        BigDecimal finalPrice;

        if (isPg) {
            listingType = ListingType.RENT;
            propertyType = PropertyType.PG;
            priceType = PriceType.MONTHLY;
            finalPrice = BigDecimal.ZERO;
        } else {
            priceType = (listingType == ListingType.SALE) ? PriceType.ONE_TIME : PriceType.MONTHLY;
            finalPrice = propertyDto.getPrice();

            if (finalPrice == null || finalPrice.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException("Price must be greater than zero.");
            }
        }

        String imagePath = null;
        if (image != null && !image.isEmpty()) {
            imagePath = "/uploads/" + saveFile(image);
        }

        Property property = Property.builder()
                .owner(owner)
                .location(location)
                .name(propertyDto.getName())
                .description(propertyDto.getDescription())
                .address(propertyDto.getAddress())
                .listingType(listingType)
                .propertyType(propertyType)
                .pgListing(isPg)
                .priceType(priceType)
                .price(finalPrice)
                .sizeSqFt(propertyDto.getSizeSqFt())
                .bhk(propertyDto.getBhk())
                .imagePath(imagePath)
                .status(PropertyStatus.PENDING)
                .build();

        return propertyRepository.save(property);
    }

    // ================= UPDATE =================
    @Override
    public Property updateProperty(Long id, String ownerEmail, PropertyDto dto, MultipartFile image) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        Location location = locationRepository.findById(dto.getLocationId())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        property.setName(dto.getName());
        property.setDescription(dto.getDescription());
        property.setAddress(dto.getAddress());
        property.setLocation(location);
        property.setListingType(dto.getListingType());
        property.setPropertyType(dto.getPropertyType());
        property.setPgListing(dto.isPgListing());
        property.setPrice(dto.getPrice());
        property.setSizeSqFt(dto.getSizeSqFt());
        property.setBhk(dto.getBhk());

        // ✅ Replace image if new one uploaded
        if (image != null && !image.isEmpty()) {
            property.setImagePath("/uploads/" + saveFile(image));
        }

        return propertyRepository.save(property);
    }

    // ================= DELETE =================
    @Override
    public void deleteProperty(Long id, String ownerEmail) {

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (!property.getOwner().getEmail().equals(ownerEmail)) {
            throw new RuntimeException("Unauthorized access");
        }

        propertyRepository.delete(property);
    }

    // ================= EXISTING =================
    @Override
    public List<Property> getOwnerProperties(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found."));
        return propertyRepository.findByOwner(owner);
    }

    @Override
    public List<Property> getOwnerPropertiesByStatus(String ownerEmail, PropertyStatus status) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found."));
        return propertyRepository.findByOwnerAndStatus(owner, status);
    }

    @Override
    public long countOwnerProperties(String ownerEmail) {
        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found."));
        return propertyRepository.countByOwner(owner);
    }

    @Override
    public List<Property> getAllPropertiesByStatus(PropertyStatus status) {
        return propertyRepository.findByStatus(status);
    }

    @Override
    public Property updatePropertyStatus(Long propertyId, PropertyStatus status) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found."));
        property.setStatus(status);
        return propertyRepository.save(property);
    }

    @Override
    public Property getPropertyById(Long id) {
        return propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
    }

    @Override
    public List<Property> getAll() {
        return propertyRepository.findAll();
    }

    @Override
    public List<Property> searchByName(String keyword) {
        return propertyRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Property> findByLocation(String location) {
        return propertyRepository.findByLocation_Name(location);
    }

    @Override
    public List<String> getAllLocations() {
        return propertyRepository.findDistinctLocations();
    }

    @Override
    public long countPropertiesByStatus(PropertyStatus status) {
        return propertyRepository.countByStatus(status);
    }

    @Override
    public List<Property> searchApprovedProperties(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return propertyRepository.findByStatus(PropertyStatus.APPROVED);
        }
        return propertyRepository.findByStatusAndNameContainingIgnoreCase(PropertyStatus.APPROVED, keyword);
    }

    // ================= FILE SAVE =================
    private String saveFile(MultipartFile file) {

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";

        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        String fileName = UUID.randomUUID() + extension;

        try {
            Path uploadPath = Paths.get(System.getProperty("user.dir"), uploadDir)
                    .toAbsolutePath().normalize();

            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(fileName);
            file.transferTo(filePath.toFile());

        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }

        return fileName;
    }
}