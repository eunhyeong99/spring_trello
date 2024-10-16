package com.sparta.spring_trello.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public User(String email, String encodedPassword, UserRole userRole) {
        this.email = email;
        this.password = encodedPassword;
        this.userRole = userRole;
    }
}
