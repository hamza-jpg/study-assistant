package com.studyassistant.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private final String testSecret = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private final long expirationMs = 3600000; // 1 hour

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider(testSecret, expirationMs);
    }

    @Test
    @DisplayName("Should generate and validate JWT token successfully")
    void testGenerateAndValidateToken() {
        UUID userId = UUID.randomUUID();
        String email = "student@example.com";
        String role = "ROLE_STUDENT";

        String token = tokenProvider.generateToken(userId, email, role);

        assertThat(token).isNotBlank();
        assertThat(tokenProvider.validateToken(token)).isTrue();
        assertThat(tokenProvider.getUserIdFromToken(token)).isEqualTo(userId);
    }

    @Test
    @DisplayName("Should reject invalid or forged token")
    void testInvalidToken() {
        assertThat(tokenProvider.validateToken("invalid.token.structure")).isFalse();
        assertThat(tokenProvider.validateToken("")).isFalse();
    }
}
