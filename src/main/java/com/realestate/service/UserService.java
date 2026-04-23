package com.realestate.service;

import com.realestate.entity.User;
import com.realestate.enums.Role;

public interface UserService {

    User registerUser(User user, Role role);
}