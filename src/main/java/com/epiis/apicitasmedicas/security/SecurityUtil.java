package com.epiis.apicitasmedicas.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil() {}

    public static String getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        return (String) auth.getPrincipal();
    }

    public static String getCurrentUserRole() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getAuthorities().isEmpty()) return null;
        String authority = auth.getAuthorities().iterator().next().getAuthority();
        return authority.replace("ROLE_", "");
    }
}
