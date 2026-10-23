package com.josepereira.inventory_admin_suite.repository;

import com.josepereira.inventory_admin_suite.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(String name);
}
