package com.josepereira.inventory_admin_suite.controller;

import com.josepereira.inventory_admin_suite.dto.UserRequestDTO;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Users");
        model.addAttribute("view", "users/list");

        if(!model.containsAttribute("uersRequest")) {
            model.addAttribute("userRequest", new UserRequestDTO());
        }
        return "layout";
    }

    @PostMapping
    public String createUser(
            @Valid @ModelAttribute("userRequest") UserRequestDTO userRequestDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
         model.addAttribute("pageTitle", "Users");
         model.addAttribute("view", "users/list");

        if (userRequestDTO.getPassword() != null &&
                !userRequestDTO.getPassword().equals(userRequestDTO.getConfirmPassword())) {
            bindingResult.rejectValue("password", "error.userRequest", "Passwords do not match");
            bindingResult.rejectValue("confirmPassword", "error.userRequest", "Passwords do not match");
        }

        // TODO: Delegate business logic to UserService upon JPA repository integration
        if(bindingResult.hasErrors()) {
            model.addAttribute("showModal", true);
                    return "layout";
        }

        redirectAttributes.addFlashAttribute("successMessage",
                "Usuario" + userRequestDTO.getFullName() + " creado con exito.");

        return "redirect:/users";
    }
}
