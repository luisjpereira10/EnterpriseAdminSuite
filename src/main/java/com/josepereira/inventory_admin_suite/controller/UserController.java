package com.josepereira.inventory_admin_suite.controller;

import com.josepereira.inventory_admin_suite.dto.UserCreateDTO;
import com.josepereira.inventory_admin_suite.dto.UserUpdateDTO;
import com.josepereira.inventory_admin_suite.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void populateCommonModel(Model model) {
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("view", "users/list");
        if (!model.containsAttribute("users")) {
            model.addAttribute("users", userService.getAllUsers());
        }
    }

    @GetMapping
    public String list(Model model) {
        if (!model.containsAttribute("userRequest")) {
            model.addAttribute("userRequest", new UserCreateDTO());
        }
        return "layout";
    }

    @PostMapping
    public String createUser(
            @Valid @ModelAttribute("userRequest") UserCreateDTO dto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        validatePasswordMatch(dto.getPassword(), dto.getConfirmPassword(), bindingResult, "userRequest");

        if (bindingResult.hasErrors()) {
            model.addAttribute("showModal", true);
            return "layout";
        }

        userService.userCreated(dto);
        redirectAttributes.addFlashAttribute("successMessage",
                "User " + dto.getFullName() + " successfully created.");
        return "redirect:/users";
    }

    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("userRequest") UserUpdateDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        validatePasswordMatch(dto.getPassword(), dto.getConfirmPassword(), bindingResult, "userRequest");

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

        if(userService.deleteUser(id)){
            redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        }else {
            redirectAttributes.addFlashAttribute("errorMensaje", "User not found with id:" +id);
        }

        return "redirect:/users";
    }

    private void validatePasswordMatch(String password, String confirmPassword, BindingResult bindingResult, String objectName) {
        if (password != null && !password.equals(confirmPassword)) {
            bindingResult.rejectValue("password", objectName, "Passwords do not match");
            bindingResult.rejectValue("confirmPassword", objectName, "Passwords do not match.");
        }
    }
}
