package com.josepereira.inventory_admin_suite.config;

import com.josepereira.inventory_admin_suite.entity.User;
import com.josepereira.inventory_admin_suite.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                User defaultUser = User.builder()
                        .fullName("José Pereira")
                        .email("jose@admin.com")
                        .role("ADMIN")
                        .password("12345678")
                        .build();

                userRepository.save(defaultUser);
                System.out.println("✅ Database initialized: User 'José Pereira' successfully saved..");
            }
        };
    }

}
