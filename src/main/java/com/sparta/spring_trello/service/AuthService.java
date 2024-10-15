package com.sparta.spring_trello.service;

import com.sparta.spring_trello.config.JwtUtil;
import com.sparta.spring_trello.dto.SigninRequest;
import com.sparta.spring_trello.dto.SignupRequest;
import com.sparta.spring_trello.entity.User;
import com.sparta.spring_trello.entity.UserRole;
import com.sparta.spring_trello.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public String signup(SignupRequest signupRequest) {
        User newUser = new User(signupRequest.getEmail(), UserRole.of(signupRequest.getUserRole()));
        User saveduser = userRepository.save(newUser);

        return jwtUtil.createToken(saveduser.getId(), saveduser.getEmail(), saveduser.getUserRole());
    }

    @Transactional(readOnly = true)
    public String signin(SigninRequest signinRequest) {
        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());
    }
}
