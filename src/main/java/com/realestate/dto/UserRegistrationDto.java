package com.realestate.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDto {

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    @Email(message = "Enter valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Contact number is required")
    private String contactNo;

    @NotBlank(message = "Password is required")
    private String password;
}