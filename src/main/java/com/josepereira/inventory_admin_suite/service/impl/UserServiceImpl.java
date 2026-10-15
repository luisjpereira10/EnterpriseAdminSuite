package com.josepereira.inventory_admin_suite.service.impl;

import com.josepereira.inventory_admin_suite.dto.UserCreateDTO;
import com.josepereira.inventory_admin_suite.dto.UserUpdateDTO;
import com.josepereira.inventory_admin_suite.entity.User;
import com.josepereira.inventory_admin_suite.repository.UserRepository;
import com.josepereira.inventory_admin_suite.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User userCreated(UserCreateDTO dto) {
        String sanitizedEmail = dto.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmail(sanitizedEmail)){
            throw new IllegalArgumentException("Email addres is already in use: ");
        }

        // TODO: [v0.4] Replace manual entity mapping with MapStruct to cleanly decouple DTOs and Entities
        User user = new User();
        user.setFullName(dto.getFullName().trim().toLowerCase());
        user.setEmail(sanitizedEmail);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole().trim().toLowerCase());

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> updateUser(Long id, UserUpdateDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String sanitizedEmail = dto.getEmail().trim().toLowerCase();

        if(userRepository.existsByEmailAndIdNot(sanitizedEmail, id)){
            throw new IllegalArgumentException("Email addres is already in use by another user " + sanitizedEmail);
        }

        // TODO: [v0.4] Replace manual entity mapping with MapStruct to cleanly decouple DTOs and Entities
        user.setFullName(dto.getFullName().trim().toLowerCase());
        user.setEmail(sanitizedEmail);
        user.setRole(dto.getRole().trim().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        if(dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return Optional.of(userRepository.save(user));
    }

    @Override
    public boolean deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
        return true;
    }
}
