package com.example.APISkeleton.security.entities;

import com.example.APISkeleton.persistance.entities.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

public class UseDetailsImpl implements UserDetails {
    @Getter
    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public UseDetailsImpl(User user) {
        this.user = user;
        this.authorities = user.getUserRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Cambiado de false a true para permitir autenticación
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}