package com.josepereira.inventory_admin_suite.service.impl;

import com.josepereira.inventory_admin_suite.dto.UserRequestDTO;
import com.josepereira.inventory_admin_suite.entity.User;
import com.josepereira.inventory_admin_suite.repository.UserRepository;
import com.josepereira.inventory_admin_suite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User userCreated(UserRequestDTO dto) {
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setRole(dto.getRole());
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
    public Optional<User> updateUser(Long id, UserRequestDTO dto) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFullName(dto.getFullName());
            existingUser.setEmail(dto.getEmail());
            existingUser.setRole(dto.getRole());
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                existingUser.setPassword(dto.getPassword());
            }
            return userRepository.save(existingUser);
        });
    }

    @Override
    public boolean deleteUser(Long id) {
        if(userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
