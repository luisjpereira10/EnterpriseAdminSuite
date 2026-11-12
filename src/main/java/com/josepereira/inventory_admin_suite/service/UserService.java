package com.josepereira.inventory_admin_suite.service;

import com.josepereira.inventory_admin_suite.dto.request.UserCreateDTO;
import com.josepereira.inventory_admin_suite.dto.request.UserUpdateDTO;
import com.josepereira.inventory_admin_suite.entity.Role;
import com.josepereira.inventory_admin_suite.entity.User;
import java.util.List;

public interface UserService {
    User userCreated(UserCreateDTO userCreateDTO);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, UserUpdateDTO userUpdateDTOdto);
    boolean deleteUser(Long id);
    List<Role> getAllRoles();

}
