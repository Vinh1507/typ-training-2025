package com.oj.application.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Custom UserDetails that carries user ID and class IDs from JWT claims
 */
public class ClassAwareUser implements UserDetails {
    private final UserDetails delegate;
    private final Long userId;
    private final List<Long> classIds;

    public ClassAwareUser(UserDetails delegate, Long userId, List<Long> classIds) {
        this.delegate = delegate;
        this.userId = userId;
        this.classIds = classIds != null ? classIds : List.of();
    }

    public Long getUserId() {
        return userId;
    }

    public List<Long> getClassIds() {
        return classIds;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getPassword() {
        return delegate.getPassword();
    }

    @Override
    public String getUsername() {
        return delegate.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return delegate.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return delegate.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return delegate.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return delegate.isEnabled();
    }
}
