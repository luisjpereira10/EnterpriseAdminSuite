package com.josepereira.inventory_admin_suite.controller;

import com.josepereira.inventory_admin_suite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller 
public class DashboardController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/", "/dashboard"})
    public String index(Model model) {
        model.addAttribute("pageTitle", "Admin Suite");
        model.addAttribute("view", "dashboard/index");
        model.addAttribute("countUsers", userRepository.count());
        return "layout";
    }
}
