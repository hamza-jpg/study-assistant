package com.studyassistant.service;

import com.studyassistant.dto.auth.AuthResponse;
import com.studyassistant.dto.auth.LoginRequest;
import com.studyassistant.dto.auth.RegisterRequest;
import com.studyassistant.dto.auth.UserDto;
import com.studyassistant.entity.Role;
import com.studyassistant.entity.User;
import com.studyassistant.repository.UserRepository;
import com.studyassistant.security.JwtTokenProvider;
import com.studyassistant.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email is already registered: " + request.getEmail());
        }

        Role role = Role.ROLE_STUDENT;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            try {
                role = Role.valueOf(request.getRole());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid role passed: {}. Defaulting to ROLE_STUDENT.", request.getRole());
            }
        }

        User user = User.builder()
            .email(request.getEmail().toLowerCase().trim())
            .passwordHash(passwordEncoder.encode(request.getPassword()))
            .fullName(request.getFullName().trim())
            .role(role)
            .build();

        User savedUser = userRepository.save(user);

        String token = tokenProvider.generateToken(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole().name()
        );

        return AuthResponse.builder()
            .token(token)
            .tokenType("Bearer")
            .user(UserDto.fromEntity(savedUser))
            .build();
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail().toLowerCase().trim(),
                request.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(principal.getId())
            .orElseThrow(() -> new IllegalStateException("User not found: " + principal.getId()));

        String token = tokenProvider.generateToken(authentication);

        return AuthResponse.builder()
            .token(token)
            .tokenType("Bearer")
            .user(UserDto.fromEntity(user))
            .build();
    }

    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new IllegalStateException("No authenticated user found in SecurityContext");
        }
        return userRepository.findById(principal.getId())
            .orElseThrow(() -> new IllegalStateException("User not found: " + principal.getId()));
    }
}
