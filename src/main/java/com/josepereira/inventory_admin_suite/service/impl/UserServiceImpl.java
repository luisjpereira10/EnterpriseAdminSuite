package com.josepereira.inventory_admin_suite.service.impl;

import com.josepereira.inventory_admin_suite.dto.UserCreateDTO;
import com.josepereira.inventory_admin_suite.dto.UserUpdateDTO;
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
    public User userCreated(UserCreateDTO userCreateDTO) {
        User user = new User();
        user.setFullName(userCreateDTO.getFullName());
        user.setEmail(userCreateDTO.getEmail());
        user.setPassword(userCreateDTO.getPassword());
        user.setRole(userCreateDTO.getRole());
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
    public Optional<User> updateUser(Long id, UserUpdateDTO userUpdateDTOdto) {
        return userRepository.findById(id).map(existingUser -> {
            existingUser.setFullName(userUpdateDTOdto.getFullName());
            existingUser.setEmail(userUpdateDTOdto.getEmail());
            existingUser.setRole(userUpdateDTOdto.getRole());
            if (userUpdateDTOdto.getPassword() != null && !userUpdateDTOdto.getPassword().isBlank()) {
                existingUser.setPassword(userUpdateDTOdto.getPassword());
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
