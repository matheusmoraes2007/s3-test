package com.tech.s3test.util;

import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {
    public static String getEmail() {
        if (SecurityContextHolder.getContext() == null
                || SecurityContextHolder.getContext().getAuthentication() == null
                || "anonymousUser".equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
        ) {
            return null;
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
