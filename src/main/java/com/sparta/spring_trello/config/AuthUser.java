package com.sparta.spring_trello.config;

import com.sparta.spring_trello.domain.member.entity.MemberRole;
import com.sparta.spring_trello.domain.user.entity.UserRole;
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
    private final MemberRole role;

    public AuthUser(Long userId, String email, MemberRole role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }

    // 사용자가 읽기 전용 역할을 가지고 있는지 확인하는 메서드
    public boolean isReadOnly() {
        return this.role == MemberRole.READONLY;
    }
}
