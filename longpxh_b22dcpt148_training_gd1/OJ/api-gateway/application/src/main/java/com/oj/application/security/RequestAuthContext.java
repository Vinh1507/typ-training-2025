package com.oj.application.security;

import java.util.List;

/**
 * Interface for accessing authenticated user context in controllers
 */
public interface RequestAuthContext {
    /**
     * Get current authenticated user ID
     * @return user ID or null if not authenticated
     */
    Long currentUserId();

    /**
     * Get current authenticated username
     * @return username or null if not authenticated
     */
    String currentUsername();

    /**
     * Get class IDs that the current user belongs to
     * @return list of class IDs, empty list if user has no classes
     */
    List<Long> currentUserClassIds();
}
