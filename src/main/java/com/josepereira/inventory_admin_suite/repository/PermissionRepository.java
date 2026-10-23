package com.josepereira.inventory_admin_suite.repository;

import com.josepereira.inventory_admin_suite.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Optional<Permission> findByName(String name);
}
