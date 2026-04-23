package com.realestate.service;

import com.realestate.dto.OwnerProfileDto;
import com.realestate.entity.OwnerProfile;

public interface OwnerProfileService {

    OwnerProfile saveOrUpdateProfile(String email, OwnerProfileDto ownerProfileDto);

    OwnerProfile getProfileByUserEmail(String email);

    boolean profileExists(String email);
}