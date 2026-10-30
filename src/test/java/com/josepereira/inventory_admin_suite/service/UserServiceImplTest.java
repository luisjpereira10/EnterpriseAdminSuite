package com.josepereira.inventory_admin_suite.service;

import com.josepereira.inventory_admin_suite.dto.UserCreateDTO;
import com.josepereira.inventory_admin_suite.dto.UserUpdateDTO;
import com.josepereira.inventory_admin_suite.entity.Role;
import com.josepereira.inventory_admin_suite.entity.User;
import com.josepereira.inventory_admin_suite.exception.ResourceNotFoundException;
import com.josepereira.inventory_admin_suite.repository.RoleRepository;
import com.josepereira.inventory_admin_suite.repository.UserRepository;
import com.josepereira.inventory_admin_suite.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private static final Role ROLE_USER = Role.builder()
            .id(1L)
            .name("USER")
            .build();

    private static final Role ROLE_ADMIN = Role.builder()
            .id(2L)
            .name("ADMIN")
            .build();

    @Test
    @DisplayName("Should successfully create a user when email is unique")
    void shouldSuccessfullyCreateUser(){
        UserCreateDTO userCreateDTO = userCreateDTO();
        String normalizeFullName = userCreateDTO.getFullName().trim().toLowerCase(Locale.ROOT);
        String normalizedEmail = userCreateDTO.getEmail().trim().toLowerCase(Locale.ROOT);

        //Given
        given(userRepository.existsByEmail(normalizedEmail)).willReturn(false);
        given(passwordEncoder.encode(userCreateDTO.getPassword())).willReturn("password123");
        given(roleRepository.findAllById(userCreateDTO.getRoleIds())).willReturn(List.of(ROLE_ADMIN));
        given(userRepository.save(any(User.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //When
        User saveUser = userService.userCreated(userCreateDTO);

        //Then
        assertThat(saveUser).isNotNull();
        verify(userRepository, times(1)).existsByEmail(normalizedEmail);
        verify(passwordEncoder, times(1)).encode(userCreateDTO.getPassword());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        verify(roleRepository, times(1)).findAllById(userCreateDTO.getRoleIds());

        User capturedUser = userCaptor.getValue();
        assertThat(capturedUser.getFullName()).isEqualTo(normalizeFullName);
        assertThat(capturedUser.getEmail()).isEqualTo(normalizedEmail);
        assertThat(capturedUser.getPassword()).isEqualTo("password123");

        assertThat(capturedUser.getRoles()).hasSize(1).contains(ROLE_ADMIN);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when role does not exist on user creation")
    void  shouldThrowExceptionWhenRoleNotFoundOnUserCreation(){
        //Given
        UserCreateDTO userCreateDTO = userCreateDTO();

        given(userRepository.existsByEmail(any())).willReturn(false);
        given(roleRepository.findAllById(userCreateDTO.getRoleIds())).willReturn(Collections.emptyList());

        //When
        assertThatThrownBy(() -> userService.userCreated(userCreateDTO))
            .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Error: No roles found for the given IDs.");

        //then
        verify(userRepository, times(1)).existsByEmail(any());
        verify(roleRepository, times(1)).findAllById(userCreateDTO.getRoleIds());
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when trying to create user with existing email")
    void shouldThrowExceptionWhenEmailAlreadyExist(){
        UserCreateDTO userCreateDTO = userCreateDTO();
        String normalizeEmail = userCreateDTO.getEmail().trim().toLowerCase(Locale.ROOT);

        //Given
        given(userRepository.existsByEmail(normalizeEmail)).willReturn(true);

        //When & Then
        assertThatThrownBy(() -> userService.userCreated(userCreateDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email addres is already in use:");

        verify(userRepository, times(1)).existsByEmail(normalizeEmail);
        verify(passwordEncoder,never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should return user when searching by existing ID")
    void shouldReturnUserWhenSearchingById(){
        User user = createDefaultUser();

        // Given
        Long userId = 1L;
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // When
        User foundUser = userService.getUserById(userId);

        // Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser).isEqualTo(user);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user is not found by ID")
    void shouldThrowResourceNotFoundExceptionWhenUserIsNotFound(){
        Long userId = 99L;

        //Given
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");

        // Verification
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Should successfully update user and normalize fields when valid data is provided")
    void  shouldSuccessfullyUpdateUser(){
        // Given
        Long userId = 1L;
        UserUpdateDTO userUpdate = userUpdateDTO();
        User existingUser = createDefaultUser();

        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        given(passwordEncoder.encode(userUpdate.getPassword())).willReturn("password321");
        given(userRepository.save(any(User.class))).willReturn(existingUser);
        given(roleRepository.findAllById(userUpdate.getRoleIds())).willReturn(List.of(ROLE_USER));

        // When
        userService.updateUser(userId, userUpdate);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        User capturedUser = userCaptor.getValue();

        assertThat(capturedUser).isNotNull();
        assertThat(capturedUser.getFullName()).isEqualTo("test name edit");
        assertThat(capturedUser.getEmail()).isEqualTo("test_edit@example.com");
        assertThat(capturedUser.getPassword()).isEqualTo("password321");
        assertThat(capturedUser.getRoles()).hasSize(1).containsExactly(ROLE_USER);

        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, times(1)).encode(userUpdate.getPassword());
        verify(roleRepository, times(1)).findAllById(userUpdate.getRoleIds());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when role does not exist on user update")
    void  shouldThrowResourceNotFoundExceptionWhenRoleDoesNotExist(){
        //Given
        Long userId = 1L;
        UserUpdateDTO userUpdate = userUpdateDTO();
        User existingUser = createDefaultUser();
        given(userRepository.findById(userId)).willReturn(Optional.of(existingUser));
        given(roleRepository.findAllById(userUpdate.getRoleIds())).willReturn(Collections.emptyList());

        //Then
        assertThatThrownBy(() ->userService.updateUser(userId, userUpdate))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessageContaining("Error: No roles found for the given IDs.");

        verify(passwordEncoder, never()).encode(userUpdate.getPassword());
        verify(roleRepository, times(1)).findAllById(userUpdate.getRoleIds());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when updating non-existing user")
    void shouldThrowExceptionWhenUpdatingNonExistingUser(){
        // Given
        Long userId = 99L;
        UserUpdateDTO userUpdateDTO = userUpdateDTO();

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(userId, userUpdateDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id: " + userId);


        verify(userRepository, times(1)).findById(userId);
        verify(passwordEncoder, never()).encode(userUpdateDTO.getPassword());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when searching by non-existing ID")
    void shouldThrowExceptionWhenSearchingByNonExistingId(){
        // Given
        Long nonExistingId = 99L;
        given(userRepository.findById(nonExistingId)).willReturn(Optional.empty());

        //When & Then
        assertThatThrownBy( () -> userService.getUserById(nonExistingId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository, times(1)).findById(nonExistingId);
    }

    @Test
    @DisplayName("Should successfully delete user when id exist")
    void shouldSuccessfullyDeleteUser(){
        //Given
        Long userId = 1L;
        User user = createDefaultUser();
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existing user")
    void shouldThrowExceptionWhenDeletingNonExistingUser(){
        Long userId = 99L;

        //Given
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        //WHEN && THEN
        assertThatThrownBy( () -> userService.deleteUser(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User not found with id: ");

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).deleteById(userId);
    }

    private UserCreateDTO userCreateDTO () {
        return UserCreateDTO.builder()
                .fullName("test")
                .email(" TEST@example.com ")
                .password("password123")
                .roleIds(List.of(ROLE_ADMIN.getId()))
                .build();
    }

    private UserUpdateDTO userUpdateDTO() {
        return UserUpdateDTO.builder()
                .fullName(" test Name edit ")
                .email(" test_eDit@example.com ")
                .password("password321")
                .roleIds(List.of(ROLE_USER.getId()))
                .build();
    }

    private User createDefaultUser() {
        User user = new User();
        user.setId(1L);
        user.setFullName("Test User");
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setRoles(Set.of(ROLE_ADMIN));
        return user;
    }
}
