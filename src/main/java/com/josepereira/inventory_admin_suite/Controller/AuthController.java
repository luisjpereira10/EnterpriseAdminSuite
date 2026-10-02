package com.josepereira.inventory_admin_suite.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("auth/login")
    public String login() {
        return "auth/login";
    }
}
