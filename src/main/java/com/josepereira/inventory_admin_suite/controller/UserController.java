package com.josepereira.inventory_admin_suite.controller;

import com.josepereira.inventory_admin_suite.dto.UserCreateDTO;
import com.josepereira.inventory_admin_suite.dto.UserRequestDTO;
import com.josepereira.inventory_admin_suite.dto.UserUpdateDTO;
import com.josepereira.inventory_admin_suite.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String list(Model model) {

        model.addAttribute("pageTitle", "Users");
        model.addAttribute("view", "users/list");
        model.addAttribute("users", userService.getAllUsers());

        if (!model.containsAttribute("userRequest")) {
            model.addAttribute("userRequest", new UserRequestDTO());
        }
        return "layout";
    }

    @PostMapping
    public String createUser(
            @Valid @ModelAttribute("userRequest") UserCreateDTO userCreateDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("view", "users/list");
        model.addAttribute("users", userService.getAllUsers());

        if (userCreateDTO.getPassword() != null &&
                !userCreateDTO.getPassword().equals(userCreateDTO.getConfirmPassword())) {
            bindingResult.rejectValue("password", "error.userRequest", "Passwords do not match");
            bindingResult.rejectValue("confirmPassword", "error.userRequest", "Passwords do not match");
        }

        // TODO: Delegate business logic to UserService upon JPA repository integration
        if (bindingResult.hasErrors()) {
            model.addAttribute("showModal", true);
            return "layout";
        }

        try {
            userService.userCreated(userCreateDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "User " + userCreateDTO.getFullName() + " successfully created.");
            return "redirect:/users";

        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.userRequest", e.getMessage());
            model.addAttribute("showModal", true);
            model.addAttribute("users", userService.getAllUsers());
            return "users/list";
        }

    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("userRequest") UserUpdateDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("view", "users/list");
        model.addAttribute("users", userService.getAllUsers());

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                bindingResult.rejectValue("password",
                        "error.userUpdateRequest", "Passwords do not match.");
                bindingResult.rejectValue("confirmPassword",
                        "error.userUpdateRequest", "Passwords do not match.");
            }
        }

        System.out.println("LOG ERRORS: " + bindingResult.getAllErrors());

        if (bindingResult.hasErrors()) {
            model.addAttribute("showModal", true);
            return "layout";
        }

        return userService.updateUser(id, dto)
                .map(updatedUser -> {
                    redirectAttributes.addFlashAttribute("successMessage",
                            "User updated successfully!");
                    return "redirect:/users";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage",
                            "User not found with id: " + id);
                    return "redirect:/users";
                });
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/users";
    }
}
