package com.studyassistant.service;

import com.studyassistant.dto.auth.AuthResponse;
import com.studyassistant.dto.auth.RegisterRequest;
import com.studyassistant.entity.Role;
import com.studyassistant.entity.User;
import com.studyassistant.repository.UserRepository;
import com.studyassistant.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequest.builder()
            .email("test@example.com")
            .password("secret123")
            .fullName("Jane Doe")
            .role("ROLE_STUDENT")
            .build();
    }

    @Test
    @DisplayName("Should successfully register a new user")
    void testRegisterSuccess() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encodedSecret");

        User savedUser = User.builder()
            .id(UUID.randomUUID())
            .email("test@example.com")
            .passwordHash("encodedSecret")
            .fullName("Jane Doe")
            .role(Role.ROLE_STUDENT)
            .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenProvider.generateToken(any(UUID.class), anyString(), anyString())).thenReturn("mock.jwt.token");

        AuthResponse response = authService.register(registerRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mock.jwt.token");
        assertThat(response.getUser().getEmail()).isEqualTo("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Should reject registration if email is already taken")
    void testRegisterDuplicateEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Email is already registered");

        verify(userRepository, never()).save(any(User.class));
    }
}
