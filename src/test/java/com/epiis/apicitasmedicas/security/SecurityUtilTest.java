package com.epiis.apicitasmedicas.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class SecurityUtilTest {

    private SecurityContext originalContext;

    @BeforeEach
    void setUp() {
        originalContext = SecurityContextHolder.getContext();
        SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.setContext(originalContext);
    }

    @Test
    void testGetCurrentUserId_WhenAuthenticated() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken("user-123", "password", Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        assertEquals("user-123", SecurityUtil.getCurrentUserId());
    }

    @Test
    void testGetCurrentUserId_WhenNotAuthenticated() {
        assertNull(SecurityUtil.getCurrentUserId());
    }

    @Test
    void testGetCurrentUserRole_WhenAuthenticatedWithRole() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            "user-123", 
            "password", 
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMINISTRADOR"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        assertEquals("ADMINISTRADOR", SecurityUtil.getCurrentUserRole());
    }

    @Test
    void testGetCurrentUserRole_WhenAuthenticatedWithoutRole() {
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            "user-123", 
            "password", 
            Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        
        assertNull(SecurityUtil.getCurrentUserRole());
    }

    @Test
    void testGetCurrentUserRole_WhenNotAuthenticated() {
        assertNull(SecurityUtil.getCurrentUserRole());
    }
}
