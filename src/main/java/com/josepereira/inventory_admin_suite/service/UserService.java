package com.josepereira.inventory_admin_suite.service;

import com.josepereira.inventory_admin_suite.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User userCreated(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    Optional<User> updateUser(Long id, User userDetails);
    boolean deleteUser(Long id);

}
