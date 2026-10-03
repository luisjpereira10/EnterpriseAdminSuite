package com.josepereira.inventory_admin_suite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String list(Model model) {
        model.addAttribute("pageTitle", "Dashboard | Users");
        model.addAttribute("view", "users/list");
        return "layout";
    }
}
