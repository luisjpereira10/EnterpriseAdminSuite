package com.josepereira.inventory_admin_suite.controller;

import com.josepereira.inventory_admin_suite.dto.UserCreateDTO;
import com.josepereira.inventory_admin_suite.dto.UserUpdateDTO;
import com.josepereira.inventory_admin_suite.exception.ResourceNotFoundException;
import com.josepereira.inventory_admin_suite.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("GET /users - Should return layout view with user list and common model attributes")
    void shouldReturnLayoutWithUsersList() throws Exception {
        when(userService.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("pageTitle", "Users"))
                .andExpect(model().attribute("view", "users/list"))
                .andExpect(model().attributeExists("users"))
                .andExpect(model().attributeExists("userRequest"));

        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("POST /users - Should create user and redirect when request is valid")
    void shouldCreateUserAndRedirectWhenValid() throws Exception {
        mockMvc.perform(post("/users")
                        .param("fullName", "John Doe")
                        .param("username", "johndoe")
                        .param("email", "john@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "password123")
                        .param("role", "user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attributeExists("successMessage"));

        verify(userService, times(1)).userCreated(any(UserCreateDTO.class));
    }

    @Test
    @DisplayName("POST /users - Should return layout with showModal when passwords do not match")
    void shouldReturnLayoutWhenPasswordMismatchOnCreate() throws Exception {
        mockMvc.perform(post("/users")
                        .param("fullName", "John Doe")
                        .param("username", "johndoe")
                        .param("email", "john@example.com")
                        .param("password", "password123")
                        .param("confirmPassword", "differentPassword")
                        .param("role", "user"))
                .andExpect(status().isOk())
                .andExpect(view().name("layout"))
                .andExpect(model().attribute("showModal", true))
                .andExpect(model().attributeHasFieldErrors("userRequest", "password", "confirmPassword"));

        verify(userService, never()).userCreated(any(UserCreateDTO.class));
    }

    @Test
    @DisplayName("POST /users/update/{id} - Should update user and redirect when request is valid")
    void shouldUpdateUserAndRedirectWhenValid() throws Exception {
        Long userId = 1L;

        mockMvc.perform(post("/users/update/{id}", userId)
                        .param("fullName", "Jane Doe")
                        .param("username", "janedoe")
                        .param("email", "jane@example.com")
                        .param("password", "newpassword123")
                        .param("confirmPassword", "newpassword123")
                        .param("role", "user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("successMessage", "User updated successfully!"));

        verify(userService, times(1)).updateUser(eq(userId), any(UserUpdateDTO.class));
    }

    @Test
    @DisplayName("POST /users/update/{id} - Should set errorMessage in flash attributes when user is not found")
    void shouldHandleResourceNotFoundOnUpdate() throws Exception {
        Long userId = 99L;
        doThrow(new ResourceNotFoundException("User not found"))
                .when(userService).updateUser(eq(userId), any(UserUpdateDTO.class));

        mockMvc.perform(post("/users/update/{id}", userId)
                        .param("fullName", "Jane Doe")
                        .param("username", "janedoe")
                        .param("email", "jane@example.com")
                        .param("password", "newpassword123")
                        .param("confirmPassword", "newpassword123")
                        .param("role", "user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("errorMessage", "User not found"));
    }

    @Test
    @DisplayName("GET /users/delete/{id} - Should delete user and set success message")
    void shouldDeleteUserSuccessfully() throws Exception {
        Long userId = 1L;
        when(userService.deleteUser(userId)).thenReturn(true);

        mockMvc.perform(get("/users/delete/{id}", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("successMessage", "User deleted successfully!"));

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("GET /users/delete/{id} - Should set error message when user deletion fails")
    void shouldSetErrorMessageWhenUserNotFoundOnDelete() throws Exception {
        Long userId = 99L;
        when(userService.deleteUser(userId)).thenReturn(false);

        mockMvc.perform(get("/users/delete/{id}", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users"))
                .andExpect(flash().attribute("errorMensaje", "User not found with id:" + userId));

        verify(userService, times(1)).deleteUser(userId);
    }
}
