package com.fitness.userservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Email is required")
    private String email;
    private String password;
    private String firstName;
    private String lastName;
}
