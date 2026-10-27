package com.josepereira.inventory_admin_suite.service.impl;

import com.josepereira.inventory_admin_suite.dto.UserCreateDTO;
import com.josepereira.inventory_admin_suite.dto.UserUpdateDTO;
import com.josepereira.inventory_admin_suite.entity.Role;
import com.josepereira.inventory_admin_suite.entity.User;
import com.josepereira.inventory_admin_suite.exception.ResourceNotFoundException;
import com.josepereira.inventory_admin_suite.repository.RoleRepository;
import com.josepereira.inventory_admin_suite.repository.UserRepository;
import com.josepereira.inventory_admin_suite.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User userCreated(UserCreateDTO dto) {
        String sanitizedEmail = dto.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmail(sanitizedEmail)){
            throw new IllegalArgumentException("Email addres is already in use: ");
        }

        List<Role> foundRoles = roleRepository.findAllById(dto.getRoleIds());
        if(foundRoles.isEmpty()){
            throw new ResourceNotFoundException("Error: No roles found for the given IDs.");
        }

        // TODO: Replace manual entity mapping with MapStruct to cleanly decouple DTOs and Entities
        User user = new User();
        user.setFullName(dto.getFullName().trim().toLowerCase());
        user.setEmail(sanitizedEmail);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRoles(new  HashSet<>(foundRoles));
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public User updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        String sanitizedEmail = dto.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmailAndIdNot(sanitizedEmail, id)){
            throw new IllegalArgumentException("Email addres is already in use by another user " + sanitizedEmail);
        }

        List<Role> foundRoles = roleRepository.findAllById(dto.getRoleIds());
        if(foundRoles.isEmpty()){
            throw new ResourceNotFoundException("Error: No roles found for the given IDs.");
        }

        // TODO: Replace manual entity mapping with MapStruct to cleanly decouple DTOs and Entities
        user.setFullName(dto.getFullName().trim().toLowerCase());
        user.setEmail(sanitizedEmail);
        user.setRoles(new HashSet<>(foundRoles));

        if(dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        User user = userRepository.findById(id)
                        .orElseThrow( ()-> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.deleteById(id);
        return true;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }
}
