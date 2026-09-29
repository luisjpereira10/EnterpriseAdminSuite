package com.josepereira.inventory_admin_suite.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class DashboardController {

    @GetMapping("/")
    public String index() {
        return "layout";
    }
}
