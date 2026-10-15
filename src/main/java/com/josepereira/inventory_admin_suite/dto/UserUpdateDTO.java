package com.josepereira.inventory_admin_suite.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email addres format")
    private String email;

    private String password;

    private String confirmPassword;

    @NotBlank(message = "User role must be selected")
    private String role;
}
