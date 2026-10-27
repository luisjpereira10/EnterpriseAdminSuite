package com.josepereira.inventory_admin_suite.config;

import com.josepereira.inventory_admin_suite.entity.Role;
import com.josepereira.inventory_admin_suite.entity.User;
import com.josepereira.inventory_admin_suite.repository.RoleRepository;
import com.josepereira.inventory_admin_suite.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   RoleRepository roleRepository,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {

                roleRepository.save(Role.builder().name("USER").build());
                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseGet(() -> roleRepository.save(Role.builder().name("ADMIN").build()));

                User defaultUser = User.builder()
                        .fullName("José Pereira")
                        .email("jose@admin.com")
                        .password(passwordEncoder.encode("12345678"))
                        .roles(Set.of(adminRole))
                        .build();

                userRepository.save(defaultUser);
                System.out.println("✅ Database initialized: User 'José Pereira' successfully saved..");
            }
        };
    }
}
