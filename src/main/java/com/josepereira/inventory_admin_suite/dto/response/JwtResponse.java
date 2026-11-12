package com.josepereira.inventory_admin_suite.dto.response;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String email;

    public JwtResponse(String token, String email) {
        this.token = token;
        this.email = email;
    }
}
