package com.josepereira.inventory_admin_suite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/settings")
public class SettingsController {

    @GetMapping
    public String showSettings(Model model) {
        model.addAttribute("pageTitle", "Dashboard | Settings");
        model.addAttribute("view", "settings/index");
        return "layout";
    }
}
