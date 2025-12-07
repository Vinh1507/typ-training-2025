package com.oj.application.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of RequestAuthContext that extracts user info from SecurityContext
 */
@Component
public class RequestAuthContextImpl implements RequestAuthContext {

    @Override
    public Long currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof ClassAwareUser) {
            return ((ClassAwareUser) principal).getUserId();
        }

        return null;
    }

    @Override
    public String currentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof ClassAwareUser) {
            return ((ClassAwareUser) principal).getUsername();
        }

        return auth.getName();
    }

    @Override
    public List<Long> currentUserClassIds() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return List.of();
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof ClassAwareUser) {
            return ((ClassAwareUser) principal).getClassIds();
        }

        return List.of();
    }
}
