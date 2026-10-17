package com.josepereira.inventory_admin_suite.service;

import com.josepereira.inventory_admin_suite.dto.UserCreateDTO;
import com.josepereira.inventory_admin_suite.dto.UserUpdateDTO;
import com.josepereira.inventory_admin_suite.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User userCreated(UserCreateDTO userCreateDTO);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, UserUpdateDTO userUpdateDTOdto);
    boolean deleteUser(Long id);

}
