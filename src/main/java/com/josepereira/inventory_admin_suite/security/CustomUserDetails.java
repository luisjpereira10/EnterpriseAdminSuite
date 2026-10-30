package com.josepereira.inventory_admin_suite.security;

import com.josepereira.inventory_admin_suite.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    // NOTE: Spring Security uses getUsername() as the principal identifier,
    // but our application uses email for authentication.
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    public User getUser() {
        return user;
    }
}
