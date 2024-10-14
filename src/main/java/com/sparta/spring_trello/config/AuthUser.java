package com.sparta.spring_trello.config;

import com.sparta.spring_trello.entity.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    private final Long userId;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String email, UserRole role) {
        this.userId = userId;
        this.email = email;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }
}
