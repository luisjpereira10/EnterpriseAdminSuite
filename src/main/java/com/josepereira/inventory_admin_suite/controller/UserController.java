package com.josepereira.inventory_admin_suite.controller;

import com.josepereira.inventory_admin_suite.dto.UserRequestDTO;
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

        if(!model.containsAttribute("userRequest")) {
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
         model.addAttribute("users", userService.getAllUsers());

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

        try {
            userService.userCreated(userRequestDTO);
            redirectAttributes.addFlashAttribute("successMessage",
                    "User " + userRequestDTO.getFullName() + " successfully created.");
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
            @Valid @ModelAttribute("userRequest") UserRequestDTO userRequestDTO,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        model.addAttribute("pageTitle", "Users");
        model.addAttribute("view", "users/list");
        model.addAttribute("users", userService.getAllUsers());

        // TODO: Refactor validation logic by separating DTOs into UserCreateDTO and UserUpdateDTO
        // or using Jakarta Validation Groups (@Validated(OnUpdate.class)) to avoid manual BindingResult filtering.
        if (userRequestDTO.getPassword() == null || userRequestDTO.getPassword().isBlank()) {
            BindingResult cleanBindingResult = new BeanPropertyBindingResult(userRequestDTO, "userRequest");

            bindingResult.getFieldErrors().stream()
                    .filter(error -> !error.getField().equals("password") && !error.getField().equals("confirmPassword"))
                    .forEach(cleanBindingResult::addError);

            bindingResult = cleanBindingResult;
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("showModal", true);
            return "layout";
        }

        try {
            userService.updateUser(id, userRequestDTO);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            return "redirect:/users";

        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("email", "error.userRequest", e.getMessage());
            model.addAttribute("showModal", true);
            return "layout";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/users";
    }
}
