package com.josepereira.inventory_admin_suite.service;

import com.josepereira.inventory_admin_suite.dto.UserRequestDTO;
import com.josepereira.inventory_admin_suite.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User userCreated(UserRequestDTO user);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> updateUser(Long id, UserRequestDTO dto);
    boolean deleteUser(Long id);

}
