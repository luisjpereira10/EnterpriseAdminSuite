package com.josepereira.inventory_admin_suite.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/users/list")
    public String list(Model model) {
        model.addAttribute("pageTitle", "Dashboard | Users");
        model.addAttribute("view", "users/list");
        return "layout";
    }
}
