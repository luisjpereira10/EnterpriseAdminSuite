package com.josepereira.inventory_admin_suite.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email addres format")
    private String email;

    private String password;

    private String confirmPassword;

    @NotEmpty(message = "You must select at least one role")
    private List<Long> roleIds;
}
