package com.epiis.apicitasmedicas.security;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    
    private final String secret = "MediCitaSecretKeyParaFirmarTokensJWT2026ErickZunigaEspinoza";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secretKey", secret);
    }

    @Test
    void testGenerateAndValidateToken() {
        String token = jwtUtil.generateToken("user123", "test@mail.com", "ADMINISTRADOR");

        assertNotNull(token);
        assertTrue(jwtUtil.validateToken(token));
        assertEquals("user123", jwtUtil.extractIdUser(token));
        assertEquals("test@mail.com", jwtUtil.extractEmail(token));
        assertEquals("ADMINISTRADOR", jwtUtil.extractRole(token));
        assertFalse(jwtUtil.isTokenExpired(token));
    }

    @Test
    void testValidateToken_Invalid() {
        assertFalse(jwtUtil.validateToken("invalidToken"));
    }
}
