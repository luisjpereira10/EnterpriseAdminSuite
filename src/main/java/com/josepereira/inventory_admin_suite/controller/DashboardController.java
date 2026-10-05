package com.josepereira.inventory_admin_suite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class DashboardController {

    @GetMapping({"/", "/dashboard"})
    public String index(Model model) {
        model.addAttribute("pageTitle", "Admin Suite");
        model.addAttribute("view", "dashboard/index");
        return "layout";
    }
}
